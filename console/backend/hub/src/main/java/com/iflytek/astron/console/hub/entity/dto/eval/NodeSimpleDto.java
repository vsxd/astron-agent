package com.iflytek.astron.console.hub.entity.dto.eval;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeSimpleDto {
    String nodeId;
    String nodeName;

    @Deprecated
    String domain;
}
