package com.iflytek.astron.console.hub.entity.core.openapi;

import lombok.Data;

import java.util.Map;

@Data
public class RequestBody {
    Boolean required = true;
    Map<String, MediaType> content;
}
