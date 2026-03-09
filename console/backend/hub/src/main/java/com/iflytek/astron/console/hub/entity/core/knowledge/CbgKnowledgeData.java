package com.iflytek.astron.console.hub.entity.core.knowledge;

import lombok.Data;

@Data
public class CbgKnowledgeData {
    String id;
    String datasetId;
    String fileId;
    String createTime;
    String updateTime;
    String chunkType;
    String content;
    String question;
    String answer;
    String dataIndex;
    String imgReference;
    String copiedFrom;
}
