package com.iflytek.astron.console.hub.entity.table.workflow.node;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("TableWorkflowIntentChain")
public class IntentChain {
    String id;
    Integer intentType;
    String name;
    String description;

    String nameErrMsg;
    String descriptionErrMsg;
}
