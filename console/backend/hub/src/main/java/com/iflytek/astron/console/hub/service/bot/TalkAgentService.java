package com.iflytek.astron.console.hub.service.bot;

import com.iflytek.astron.console.commons.constant.ResponseEnum;
import com.iflytek.astron.console.hub.dto.bot.BotInfoDto;
import com.iflytek.astron.console.hub.dto.bot.TalkAgentHistoryDto;
import com.iflytek.astron.console.hub.dto.bot.TalkAgentUpgradeDto;
import jakarta.servlet.http.HttpServletRequest;

public interface TalkAgentService {
    String getSignature();

    ResponseEnum saveHistory(String uid, TalkAgentHistoryDto talkAgentHistoryDto);

    BotInfoDto upgradeWorkflow(Integer sourceId, String uid, Long spaceId, HttpServletRequest request, TalkAgentUpgradeDto talkAgentUpgradeDto);
}
