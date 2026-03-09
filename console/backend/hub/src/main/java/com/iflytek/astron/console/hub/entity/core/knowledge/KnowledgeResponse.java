package com.iflytek.astron.console.hub.entity.core.knowledge;

import lombok.Data;

@Data
public class KnowledgeResponse {
    Integer code;
    String sid;
    String message;
    Object data;
}
