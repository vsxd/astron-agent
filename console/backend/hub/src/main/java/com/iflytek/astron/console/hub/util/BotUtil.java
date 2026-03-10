package com.iflytek.astron.console.hub.util;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.iflytek.astron.console.hub.dto.bot.BotCreateForm;
import com.iflytek.astron.console.hub.entity.bot.ChatBotBase;
import com.iflytek.astron.console.hub.entity.workflow.Workflow;
import com.iflytek.astron.console.hub.service.bot.BotService;
import com.iflytek.astron.console.hub.service.bot.ChatBotDataService;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * Bot-related utility class
 */
@Component
@RequiredArgsConstructor
public class BotUtil {

    private final ChatBotDataService chatBotDataService;

    private final BotService botService;

    public static final String BOT_INPUT_EXAMPLE_SPLIT = "%%split%%";

    public static String convertNumToStr(int number, String langCode) {
        String numStr = "";
        if (ObjectUtil.isNotNull(number)) {
            if (number >= 10000) {
                // Divide by 10000 and keep one decimal place
                numStr += "en".equals(langCode) ? NumberUtil.round(NumberUtil.div(number, 1000), 1) + "k" : NumberUtil.round(NumberUtil.div(number, 10000), 1) + "w";
            } else if (number >= 1000) {
                numStr += "en".equals(langCode) ? NumberUtil.round(NumberUtil.div(number, 1000), 1) + "k" : String.valueOf(number);
            } else {
                numStr = String.valueOf(number);
            }
        }
        return numStr;
    }

    public Integer syncToSparkDatabase(Workflow workflow, String uid, Long spaceId) {
        BotCreateForm bot = new BotCreateForm();
        ChatBotBase botBase = new ChatBotBase();
        botBase.setUid(uid);
        botBase.setBotName(workflow.getName());
        botBase.setAvatar(workflow.getAvatarIcon());
        botBase.setBotDesc(workflow.getDescription());
        botBase.setPromptType(0);
        botBase.setSupportContext(0);
        botBase.setSpaceId(spaceId);
        if (bot.getInputExample() != null && !bot.getInputExample().isEmpty()) {
            botBase.setInputExample(String.join(BOT_INPUT_EXAMPLE_SPLIT, bot.getInputExample()));
        }
        // Professional version workflow version = 3
        botBase.setVersion(3);
        botBase.setBotwebStatus(1);
        chatBotDataService.createBot(botBase);

        String flowId = workflow.getFlowId();
        JSONObject maas = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("flowId", flowId);
        data.put("id", workflow.getId());
        maas.put("data", data);
        botService.addMaasInfo(uid, maas, botBase.getId(), spaceId);
        return botBase.getId();
    }

}
