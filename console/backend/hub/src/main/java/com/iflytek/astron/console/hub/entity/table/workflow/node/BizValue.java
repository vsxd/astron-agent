package com.iflytek.astron.console.hub.entity.table.workflow.node;


import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("TableWorkflowBizValue")
public class BizValue {
    String type;
    Object content;
    String contentErrMsg;
}
