package com.iflytek.astron.console.hub.entity.dto.openapi;

import lombok.Data;

/**
 * Request DTO for workflow IO transformation query
 */
@Data
public class WorkflowIoTransRequest {

    /**
     * API Key extracted from authorization header
     */
    private String apiKey;

    /**
     * API Secret extracted from authorization header
     */
    private String apiSecret;
}
