package com.iflytek.astron.console.hub.entity.table.workflow;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("flow_protocol_temp")
public class FlowProtocolTemp {
    String flowId;
    Date createdTime;
    String bizProtocol;
    String sysProtocol;
}
