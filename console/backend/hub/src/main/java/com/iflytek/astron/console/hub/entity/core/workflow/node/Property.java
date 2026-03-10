package com.iflytek.astron.console.hub.entity.core.workflow.node;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Map;

@Data
@Alias("WorkflowNodeProperty")
public class Property {
    Map<String, Property> properties;
    String type;
    Property items;
    Object required;
}
