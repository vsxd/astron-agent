package com.iflytek.astron.console.hub.entity.tool;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("ToolText")
public class Text {
    String text;
}
