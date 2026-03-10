package com.iflytek.astron.console.hub.entity.tool;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("ToolTool")
public class Tool {
    String id;
    @JSONField(name = "schema_type")
    Integer schemaType;
    String name;
    String description;
    @JSONField(name = "openapi_schema")
    String openapiSchema;
    String version;
}
