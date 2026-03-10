package com.iflytek.astron.console.hub.entity.biz.modelconfig;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Data
@Alias("BizModel")
public class Model {
    CompletionParams completionParams;
    // String mode = "completion";
    String domain;
    List<String> patchId;
    String serviceId;
    Long llmId;
    Integer llmSource;
    String api;
    Long modelId;
}
