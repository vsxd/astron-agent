package com.iflytek.astron.console.hub.entity.table.relation;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowToolRel {
    String flowId;
    String toolId;
    String version;
}
