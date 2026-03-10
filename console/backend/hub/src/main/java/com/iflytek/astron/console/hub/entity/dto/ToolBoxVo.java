package com.iflytek.astron.console.hub.entity.dto;

import com.iflytek.astron.console.hub.entity.table.tool.ToolBox;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ToolBoxVo extends ToolBox {
    private static final long serialVersionUID = 1L;
    String address;

    List<SparkBotVo> bots;

    Boolean isFavorite;

    Integer botUsedCount;

    String creator;

    List<String> tags;

    Long heatValue;

    Boolean isMcp = false;

    String mcpTooId;

    Boolean authorized;
}
