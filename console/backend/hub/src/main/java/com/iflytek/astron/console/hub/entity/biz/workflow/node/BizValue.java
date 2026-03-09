package com.iflytek.astron.console.hub.entity.biz.workflow.node;


import lombok.Data;

@Data
public class BizValue {
    String type;
    Object content;
    String contentErrMsg;
}
