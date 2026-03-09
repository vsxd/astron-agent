package com.iflytek.astron.console.hub.entity.core.workflow.node;

import lombok.Data;

import java.util.Map;

@Data
public class Property {
    Map<String, Property> properties;
    String type;
    Property items;
    Object required;
}
