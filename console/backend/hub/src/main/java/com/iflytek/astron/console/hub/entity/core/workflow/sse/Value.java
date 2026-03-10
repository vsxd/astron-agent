package com.iflytek.astron.console.hub.entity.core.workflow.sse;

import com.alibaba.fastjson2.JSONArray;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("WorkflowSseValue")
public class Value {
    @JsonProperty("type")
    String type;
    @JsonProperty("option")
    JSONArray option;
    @JsonProperty("content")
    String content;
}
