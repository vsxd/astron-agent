package com.iflytek.astron.console.hub.entity.tool;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("ToolMessage")
public class Message {
    String header;
    String query;
    String body;
}
