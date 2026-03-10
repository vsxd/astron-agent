package com.iflytek.astron.console.hub.entity.core.workflow.node;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Map;

@Data
@Alias("WorkflowNodeSchema")
public class Schema {
    String type;
    Value value;
    @JSONField(name = "default")
    @JsonProperty("default")
    Object dft;

    String description;

    // Output-specific fields
    Map<String, Property> properties;
    Property items;
    Object required;
}
