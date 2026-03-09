package com.iflytek.astron.console.hub.dto.bot;

import lombok.Data;

@Data
public class BotCloneWorkflowDto {
    Long maasId;
    Integer botId;
    String password;
    Integer flowType;
    TalkAgentConfigDto flowConfig;
}
