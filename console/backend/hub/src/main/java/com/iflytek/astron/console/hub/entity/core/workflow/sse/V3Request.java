package com.iflytek.astron.console.hub.entity.core.workflow.sse;

import lombok.Data;

@Data
public class V3Request {

    String model;

    Object messages;

    boolean stream;

    String domain;


}
