package com.iflytek.astron.console.hub.entity.dto;

import com.iflytek.astron.console.hub.entity.mongo.PreviewKnowledge;
import com.iflytek.astron.console.hub.entity.table.repo.FileInfoV2;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PreviewKnowledgeDto extends PreviewKnowledge {
    private FileInfoV2 fileInfoV2;
}
