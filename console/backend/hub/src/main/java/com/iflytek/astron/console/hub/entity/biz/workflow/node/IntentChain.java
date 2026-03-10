package com.iflytek.astron.console.hub.entity.biz.workflow.node;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("BizWorkflowIntentChain")
public class IntentChain {
    String id;
    Integer intentType;
    String name;
    String description;

    String nameErrMsg;
    String descriptionErrMsg;
}
