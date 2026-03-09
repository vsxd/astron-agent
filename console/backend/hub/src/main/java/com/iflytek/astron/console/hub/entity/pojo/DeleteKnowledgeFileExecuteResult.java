package com.iflytek.astron.console.hub.entity.pojo;

import lombok.Data;

@Data
public class DeleteKnowledgeFileExecuteResult {
    private String sourceId;
    private Integer executeResult;
    private String failedReason;
}
