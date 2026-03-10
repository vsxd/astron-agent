package com.iflytek.astron.console.hub.entity.tool;

import lombok.Data;

import java.util.List;
import org.apache.ibatis.type.Alias;

@Data
@Alias("ToolWebSchema")
public class WebSchema {
    @Deprecated
    List<WebSchemaItem> toolHttpHeaders;
    @Deprecated
    List<WebSchemaItem> toolUrlParams;
    @Deprecated
    List<WebSchemaItem> toolRequestBody;

    List<WebSchemaItem> toolRequestInput;

    List<WebSchemaItem> toolRequestOutput;
}
