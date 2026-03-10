package com.iflytek.astron.console.hub.entity.spark;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("SparkText")
public class Text {
    String role;
    Object content;
    Integer index;
}
