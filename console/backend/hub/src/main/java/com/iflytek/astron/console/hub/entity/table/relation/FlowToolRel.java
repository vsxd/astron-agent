package com.iflytek.astron.console.hub.entity.table.relation;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("flow_tool_rel")
public class FlowToolRel {
    String flowId;
    String toolId;
    String version;
}
