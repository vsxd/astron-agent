package com.iflytek.astron.console.hub.entity.core.workflow.sse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("WorkflowSseUsage")
public class Usage {
    @JsonProperty("prompt_tokens")
    Integer promptTokens;
    @JsonProperty("completion_tokens")
    Integer completionTokens;
    @JsonProperty("total_tokens")
    Integer totalTokens;
}
