package com.iflytek.astron.console.hub.entity.core.openapi;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.List;
import java.util.Map;

@Data
@Alias("OpenApiProperty")
public class Property {
    String type;
    String description;
    Map<String, Property> properties;
    @JSONField(name = "x-from")
    Integer xFrom;
    @JSONField(name = "x-display")
    Boolean xDisplay;
    List<String> required;
    @JSONField(name = "default")
    Object dft;
    Property items;
}
