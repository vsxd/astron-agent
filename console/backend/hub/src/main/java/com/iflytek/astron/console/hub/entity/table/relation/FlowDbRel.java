package com.iflytek.astron.console.hub.entity.table.relation;

import lombok.Data;

import java.util.Date;

@Data
public class FlowDbRel {

    private Long id;

    private String dbId;

    private String flowId;

    private Long tbId;

    private Date createTime;

    private Date updateTime;
}
