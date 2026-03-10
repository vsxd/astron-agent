package com.iflytek.astron.console.hub.entity.table.relation;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("flow_db_rel")
public class FlowDbRel {

    private Long id;

    private String dbId;

    private String flowId;

    private Long tbId;

    private Date createTime;

    private Date updateTime;
}
