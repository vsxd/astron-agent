package com.iflytek.astron.console.hub.entity.biz;

import lombok.Data;

@Data
public class ChatSampleDto {
    String applicationId;
    Integer applicationType;
    Long sampleStartTime;
    Long sampleEndTime;
    Integer sampleAmount;
    Integer sampleMode;
}
