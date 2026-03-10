package com.iflytek.astron.console.hub.entity.core.openapi;


import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("OpenApiParameter")
public class Parameter {
    String name;
    String in;
    String description;
    Boolean required;
    Schema schema;
}
