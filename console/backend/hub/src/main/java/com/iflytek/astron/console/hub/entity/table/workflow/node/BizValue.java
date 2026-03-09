package com.iflytek.astron.console.hub.entity.table.workflow.node;


import lombok.Data;

@Data
public class BizValue {
    String type;
    Object content;
    String contentErrMsg;
}
