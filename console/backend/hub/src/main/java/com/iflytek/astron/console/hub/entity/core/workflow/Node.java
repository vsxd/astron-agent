package com.iflytek.astron.console.hub.entity.core.workflow;


import com.iflytek.astron.console.hub.entity.core.workflow.node.NodeData;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("WorkflowNode")
public class Node {
    String id;
    NodeData data;
}
