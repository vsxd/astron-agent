package com.iflytek.astron.console.hub.entity.dto;

import com.iflytek.astron.console.hub.entity.table.repo.UploadDocTask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UploadDocTaskDto extends UploadDocTask {
    private String sourceId;
}
