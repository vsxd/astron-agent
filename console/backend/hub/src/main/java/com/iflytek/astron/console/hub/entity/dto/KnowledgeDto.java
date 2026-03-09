package com.iflytek.astron.console.hub.entity.dto;

import com.iflytek.astron.console.hub.entity.mongo.Knowledge;
import com.iflytek.astron.console.hub.entity.table.repo.FileInfoV2;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class KnowledgeDto extends Knowledge {
    private List<TagDto> tagDtoList;
    private FileInfoV2 fileInfoV2;
}
