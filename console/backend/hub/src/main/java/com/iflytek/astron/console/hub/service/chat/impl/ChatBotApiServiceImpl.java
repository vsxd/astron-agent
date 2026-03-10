package com.iflytek.astron.console.hub.service.chat.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.iflytek.astron.console.hub.dto.bot.ChatBotApi;
import com.iflytek.astron.console.hub.mapper.bot.ChatBotApiMapper;
import com.iflytek.astron.console.hub.service.chat.ChatBotApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * @author mingsuiyongheng
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatBotApiServiceImpl implements ChatBotApiService {

    private final ChatBotApiMapper chatBotApiMapper;

    /**
     * Get chat bot API list for specified user ID
     *
     * @param uid User ID
     * @return List of chat bot APIs
     */
    @Override
    public List<ChatBotApi> getBotApiList(String uid) {
        return chatBotApiMapper.selectListWithVersion(uid);
    }

    @Override
    public boolean exists(Long botId) {
        return chatBotApiMapper.exists(Wrappers.lambdaQuery(ChatBotApi.class).eq(ChatBotApi::getBotId, botId));
    }

    @Override
    public Long selectCount(Integer botId) {
        return chatBotApiMapper.selectCount(Wrappers.lambdaQuery(ChatBotApi.class).eq(ChatBotApi::getBotId, botId));
    }

    @Override
    public void insertOrUpdate(ChatBotApi chatBotApi) {
        if (chatBotApi.getCreateTime() == null) {
            chatBotApi.setCreateTime(LocalDateTime.now());
        }

        String assistantId = chatBotApi.getAssistantId();
        if (assistantId != null && chatBotApiMapper.exists(Wrappers.lambdaQuery(ChatBotApi.class).eq(ChatBotApi::getAssistantId, assistantId))) {
            chatBotApiMapper.updateById(chatBotApi);
        } else {
            chatBotApiMapper.insert(chatBotApi);
        }

    }

    @Override
    public ChatBotApi getOneByUidAndBotId(String uid, Long botId) {
        return chatBotApiMapper.selectOne(Wrappers.lambdaQuery(ChatBotApi.class)
                .eq(ChatBotApi::getBotId, botId)
                .eq(ChatBotApi::getUid, uid)
                .orderByDesc(ChatBotApi::getId)
                .last("limit 1"));
    }

}
