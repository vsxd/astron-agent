package com.iflytek.astron.console.hub.entity.tool;

import lombok.Data;

@Data
public class ToolProtocolDto {
    ToolHeader header;
    ToolParameter parameter;
    ToolPayload payload;
}
