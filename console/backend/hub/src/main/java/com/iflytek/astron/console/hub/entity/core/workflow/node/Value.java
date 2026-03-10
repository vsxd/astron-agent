package com.iflytek.astron.console.hub.entity.core.workflow.node;


import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("WorkflowNodeValue")
public class Value {
    String type;
    Object content;
}
