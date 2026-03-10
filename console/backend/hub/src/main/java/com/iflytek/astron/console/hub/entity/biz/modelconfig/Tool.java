package com.iflytek.astron.console.hub.entity.biz.modelconfig;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("BizTool")
public class Tool {
    String toolId;
    String name;
    String description;
}
