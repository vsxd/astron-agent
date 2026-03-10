package com.iflytek.astron.console.hub.entity.spark.response;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("SparkUsage")
public class Usage {
    UsageText text;
}
