package com.iflytek.astron.console.hub.entity.biz.workflow.node;


import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("BizWorkflowBizValue")
public class BizValue {
    String type;
    Object content;
    String contentErrMsg;
}
