package com.iflytek.astron.console.hub.entity.core.openapi;

import lombok.Data;

@Data
public class Server {
    private String url;
    private String description = "a server description";
}
