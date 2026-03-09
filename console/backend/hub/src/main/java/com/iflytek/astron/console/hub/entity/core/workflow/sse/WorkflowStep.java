package com.iflytek.astron.console.hub.entity.core.workflow.sse;

import lombok.Data;

@Data
public class WorkflowStep {
    Node node;
    Integer seq;
    Integer progress;
}
