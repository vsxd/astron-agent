package com.iflytek.astron.console.hub.entity.dto;

import com.iflytek.astron.console.hub.entity.table.repo.FileInfoV2;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FileInfoV2Dto extends FileInfoV2 {
    private static final long serialVersionUID = 1L;
    private Long paragraphCount;
}
