package com.iflytek.astron.console.hub.entity.core.workflow;

import lombok.Data;

@Data
public class Edge {
    String sourceNodeId;
    String targetNodeId;
    String sourceHandle;
    String targetHandle;
}
