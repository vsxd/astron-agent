package com.iflytek.astron.console.hub.dto.bot;


import lombok.Data;

@Data
public class TalkAgentHistoryDto {
    private Long chatId;
    private Integer clientType;
    private String req;
    private String resp;
    private String sid;
}
