package com.iflytek.astron.console.hub.dto.bot;

import lombok.Data;

/**
 * Prompt Structure DTO
 */
@Data
public class PromptStructDto {

    /**
     * Prompt key name
     */
    private String promptKey;

    /**
     * Prompt content
     */
    private String promptValue;
}
