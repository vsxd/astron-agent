package com.iflytek.astron.console.hub.service.user.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iflytek.astron.console.hub.dto.bot.ChatBotApi;
import com.iflytek.astron.console.hub.entity.bot.UserLangChainInfo;
import com.iflytek.astron.console.hub.entity.model.McpData;
import com.iflytek.astron.console.hub.enums.bot.ReleaseTypeEnum;
import com.iflytek.astron.console.hub.mapper.bot.ChatBotListMapper;
import com.iflytek.astron.console.hub.service.bot.BotFavoriteService;
import com.iflytek.astron.console.hub.service.bot.BotService;
import com.iflytek.astron.console.hub.service.data.UserLangChainDataService;
import com.iflytek.astron.console.hub.service.mcp.McpDataService;
import com.iflytek.astron.console.hub.util.BotUtil;
import com.iflytek.astron.console.commons.util.I18nUtil;
import com.iflytek.astron.console.commons.util.RequestContextUtil;
import com.iflytek.astron.console.hub.util.space.SpaceInfoUtil;
import com.iflytek.astron.console.hub.dto.user.MyBotPageDto;
import com.iflytek.astron.console.hub.dto.user.MyBotParamDto;
import com.iflytek.astron.console.hub.dto.user.MyBotResponseDto;
import com.iflytek.astron.console.hub.entity.ApplicationForm;
import com.iflytek.astron.console.hub.entity.wechat.BotOffiaccount;
import com.iflytek.astron.console.hub.mapper.ApplicationFormMapper;
import com.iflytek.astron.console.hub.service.wechat.BotOffiaccountService;
import com.iflytek.astron.console.hub.service.chat.ChatBotApiService;
import com.iflytek.astron.console.hub.service.user.UserBotService;
import com.iflytek.astron.console.hub.util.BotPermissionUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

/**
 * @author wowo_zZ
 * @since 2025/9/9 19:26
 **/
@Service
@RequiredArgsConstructor
public class UserBotServiceImpl implements UserBotService {

    private final ChatBotListMapper chatBotListMapper;

    private final BotOffiaccountService botOffiaccountService;

    private final UserLangChainDataService userLangChainDataService;

    private final BotFavoriteService botFavoriteService;

    private final ChatBotApiService chatBotApiService;

    private final McpDataService mcpDataService;

    private final RedissonClient redissonClient;

    private final BotPermissionUtil botPermissionUtil;

    private final ApplicationFormMapper applicationFormMapper;

    private final BotService botService;

    public static final String RECORD_BOT_ID = "recordFormBotId_";

    @Override
    public MyBotPageDto listMyBots(MyBotParamDto myBotParamDto) {
        String uid = RequestContextUtil.getUID();
        Long spaceId = SpaceInfoUtil.getSpaceId();

        // Build query parameters
        Map<String, Object> param = buildQueryParams(myBotParamDto, uid, spaceId);

        // Get count and setup pagination
        Long count = chatBotListMapper.countCheckBotList(param);
        setupPagination(param, myBotParamDto);

        // Get release information
        ReleaseInfo releaseInfo = getReleaseInfo(uid);

        // Get bot list and process
        LinkedList<Map<String, Object>> list = chatBotListMapper.getCheckBotList(param);
        Set<Integer> botIdSet = processBotList(list, releaseInfo);

        // Process chain information if needed
        if (CollectionUtils.isNotEmpty(botIdSet)) {
            processChainInformation(list, botIdSet);
        }

        // Convert to DTOs and return
        Page<MyBotResponseDto> myBotResponsesPage = createPageResult(list, count);
        return new MyBotPageDto(
                myBotResponsesPage.getRecords(),
                Math.toIntExact(myBotResponsesPage.getTotal()),
                Math.toIntExact(myBotResponsesPage.getSize()),
                Math.toIntExact(myBotResponsesPage.getCurrent()),
                Math.toIntExact(myBotResponsesPage.getPages()));
    }

    @Override
    public Boolean deleteBot(Integer botId) {
        // Permission validation
        botPermissionUtil.checkBot(botId);
        return botService.deleteBot(botId);
    }

    private Map<String, Object> buildQueryParams(MyBotParamDto myBotParamDto, String uid, Long spaceId) {
        Map<String, Object> param = getBotCheckParam(myBotParamDto, uid);
        param.put("spaceId", spaceId);

        if (myBotParamDto.getVersion() != null) {
            param.put("version", myBotParamDto.getVersion());
        }
        if (StringUtils.isNotBlank(myBotParamDto.getSearchValue())) {
            param.put("botName", myBotParamDto.getSearchValue());
        }
        if (myBotParamDto.getSort() != null) {
            if (("createTime").equals(myBotParamDto.getSort())) {
                param.put("sort", "a.create_time desc");
            }
            if (("updateTime").equals(myBotParamDto.getSort())) {
                param.put("sort", "a.update_time desc");
            }
        }
        if (CollectionUtils.isNotEmpty(myBotParamDto.getBotStatus())) {
            List<Integer> botStatus = myBotParamDto.getBotStatus();
            param.put("status", botStatus);
            if (botStatus.contains(0)) {
                param.put("flag", 1);
            }
        }
        return param;
    }

    private void setupPagination(Map<String, Object> param, MyBotParamDto myBotParamDto) {
        int pageNum = myBotParamDto.getPageIndex();
        int pageSize = Math.min(myBotParamDto.getPageSize(), 200);
        int offset = (pageNum - 1) * pageSize;
        param.put("offset", offset);
        param.put("pageSize", pageSize);
    }

    private ReleaseInfo getReleaseInfo(String uid) {
        List<Integer> favoriteBotIdList = botFavoriteService.list(uid);

        Set<Long> wechatBotId = botOffiaccountService.getAccountList(uid)
                .stream()
                .map(BotOffiaccount::getBotId)
                .map(Integer::longValue)
                .collect(Collectors.toSet());

        Set<Integer> apiBotId = chatBotApiService.getBotApiList(uid)
                .stream()
                .map(ChatBotApi::getBotId)
                .collect(Collectors.toSet());

        Set<Integer> botIdMcpSet = mcpDataService.getMcpByUid(uid)
                .stream()
                .map(McpData::getBotId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return new ReleaseInfo(favoriteBotIdList, wechatBotId, apiBotId, botIdMcpSet);
    }

    private Set<Integer> processBotList(LinkedList<Map<String, Object>> list, ReleaseInfo releaseInfo) {
        Set<Integer> botIdSet = new HashSet<>();

        for (Map<String, Object> map : list) {
            Long botId = Convert.toLong(map.get("botId"));

            // Process release types
            List<Integer> botRelease = processReleaseTypes(map, botId, releaseInfo);
            map.put("releaseType", botRelease);

            // Process application form status
            processApplicationFormStatus(map, botId);

            // Process hot number
            processHotNumber(map);

            // Process favorite status
            processFavoriteStatus(map, botId, releaseInfo.favoriteBotIdList);

            botIdSet.add((Integer) map.get("botId"));
        }

        return botIdSet;
    }

    private List<Integer> processReleaseTypes(Map<String, Object> map, Long botId, ReleaseInfo releaseInfo) {
        List<Integer> botRelease = new ArrayList<>();

        if (map.get("botStatus").equals(1L) || map.get("botStatus").equals(4L) || map.get("botStatus").equals(2L)) {
            botRelease.add(ReleaseTypeEnum.MARKET.getCode());
        }

        if (releaseInfo.wechatBotId.contains(botId)) {
            botRelease.add(ReleaseTypeEnum.WECHAT.getCode());
        }

        if (releaseInfo.apiBotId.contains(botId.intValue())) {
            botRelease.add(ReleaseTypeEnum.BOT_API.getCode());
        }

        if (releaseInfo.botIdMcpSet.contains(botId.intValue())) {
            botRelease.add(ReleaseTypeEnum.MCP.getCode());
        }

        return botRelease;
    }

    private void processApplicationFormStatus(Map<String, Object> map, Long botId) {
        RBucket<String> bucket = redissonClient.getBucket(RECORD_BOT_ID + botId);
        if (bucket.isExists()) {
            map.put("af", "1");
        } else {
            ApplicationForm applicationForm = applicationFormMapper.selectOne(
                    Wrappers.lambdaQuery(ApplicationForm.class)
                            .eq(ApplicationForm::getBotId, botId)
                            .last("limit 1"));
            map.put("af", applicationForm != null ? "1" : "0");
        }
    }

    private void processHotNumber(Map<String, Object> map) {
        int hotNum = Convert.toInt(map.get("hotNum") == null ? 0 : map.get("hotNum"), 0);
        String langCode = I18nUtil.getLanguage();
        map.put("hotNum", BotUtil.convertNumToStr(hotNum, langCode));
    }

    private void processFavoriteStatus(Map<String, Object> map, Long botId, List<Integer> favoriteBotIdList) {
        map.put("isFavorite", favoriteBotIdList.contains(botId.intValue()) ? 1 : 0);
    }

    private void processChainInformation(LinkedList<Map<String, Object>> list, Set<Integer> botIdSet) {
        List<UserLangChainInfo> chainList = userLangChainDataService.findByBotIdSet(botIdSet);
        Map<Integer, UserLangChainInfo> chainMap = chainList.stream()
                .collect(Collectors.toMap(
                        UserLangChainInfo::getBotId,
                        Function.identity(),
                        (existing, newValue) -> newValue));

        Map<Integer, Boolean> multiInputMap = chainList.stream()
                .collect(Collectors.toMap(
                        UserLangChainInfo::getBotId,
                        chain -> {
                            if (chain.getExtraInputs() != null) {
                                JSONObject extraInputs = JSONObject.parseObject(chain.getExtraInputs());
                                int size = extraInputs.size();
                                if (extraInputs.containsValue("image")) {
                                    size -= 2;
                                }
                                return size > 0;
                            } else {
                                return false;
                            }
                        }));
        list.stream()
                .filter(map -> chainMap.containsKey((Integer) map.get("botId")))
                .forEach(map -> map.put("maasId", chainMap.get(map.get("botId")).getMaasId()));

        list.forEach(map -> map.put("multiInput", multiInputMap.get(map.get("botId"))));
    }

    private Page<MyBotResponseDto> createPageResult(LinkedList<Map<String, Object>> list, Long count) {
        List<MyBotResponseDto> myBotResponseDtoList = list.stream().map(this::mapToMyBotDto).collect(Collectors.toList());

        Page<MyBotResponseDto> page = new Page<>();
        page.setTotal(count);
        page.setRecords(myBotResponseDtoList);
        return page;
    }

    private static class ReleaseInfo {
        final List<Integer> favoriteBotIdList;
        final Set<Long> wechatBotId;
        final Set<Integer> apiBotId;
        final Set<Integer> botIdMcpSet;

        ReleaseInfo(List<Integer> favoriteBotIdList, Set<Long> wechatBotId,
                Set<Integer> apiBotId, Set<Integer> botIdMcpSet) {
            this.favoriteBotIdList = favoriteBotIdList;
            this.wechatBotId = wechatBotId;
            this.apiBotId = apiBotId;
            this.botIdMcpSet = botIdMcpSet;
        }
    }

    private MyBotResponseDto mapToMyBotDto(Map<String, Object> map) {
        MyBotResponseDto dto = new MyBotResponseDto();
        dto.setBotId(Convert.toLong(map.get("botId")));
        dto.setUid(Convert.toStr(map.get("uid")));
        dto.setMarketBotId(Convert.toLong(map.get("marketBotId")));
        dto.setBotName(Convert.toStr(map.get("botName")));
        dto.setBotDesc(Convert.toStr(map.get("botDesc")));
        dto.setAvatar(Convert.toStr(map.get("avatar")));
        dto.setPrompt(Convert.toStr(map.get("prompt")));
        dto.setBotType(Convert.toInt(map.get("botType")));
        dto.setVersion(Convert.toInt(map.get("version")));
        dto.setSupportContext(Convert.toBool(map.get("supportContext")));
        dto.setMultiInput(map.get("multiInput"));
        dto.setBotStatus(Convert.toInt(map.get("botStatus")));
        dto.setBlockReason(Convert.toStr(map.get("blockReason")));
        dto.setReleaseType((List<Object>) map.get("releaseType"));
        dto.setHotNum(Convert.toStr(map.get("hotNum")));
        dto.setIsFavorite(Convert.toInt(map.get("isFavorite")));
        dto.setAf(Convert.toStr(map.get("af")));
        dto.setMaasId(Convert.toLong(map.get("maasId")));
        dto.setCreateTime((LocalDateTime) map.get("createTime"));
        return dto;
    }

    private static Map<String, Object> getBotCheckParam(MyBotParamDto myBotParamDto, String uid) {
        Map<String, Object> param = new HashMap<>();
        param.put("uid", uid);
        List<Integer> botStatuses = myBotParamDto.getBotStatus();
        if (!Objects.isNull(botStatuses) && botStatuses.contains(1)) {
            botStatuses.add(4);
        }
        param.put("botStatus", botStatuses);
        if (Objects.nonNull(botStatuses) && botStatuses.size() == 1 && botStatuses.get(0) == -9) {
            param.put("flag", 1);
        }
        return param;
    }

}
