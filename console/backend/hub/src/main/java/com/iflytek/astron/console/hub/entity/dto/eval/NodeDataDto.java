package com.iflytek.astron.console.hub.entity.dto.eval;

import com.iflytek.astron.console.hub.entity.table.trace.NodeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NodeDataDto extends NodeInfo {
    String markData;
}
