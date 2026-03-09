package com.iflytek.astron.console.hub.dto.chat;

import lombok.Data;

import java.util.LinkedList;

/**
 * @author mingsuiyongheng
 */
@Data
public class ChatRequestDtoList {
    private LinkedList<ChatRequestDto> messages = new LinkedList<>();

    /** Concatenate chat history */
    private Integer length;

    private boolean botEdit = false;
}
