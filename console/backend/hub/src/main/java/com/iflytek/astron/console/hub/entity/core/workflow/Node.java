package com.iflytek.astron.console.hub.entity.core.workflow;


import com.iflytek.astron.console.hub.entity.core.workflow.node.NodeData;
import lombok.Data;

@Data
public class Node {
    String id;
    NodeData data;
}
