package com.iflytek.astron.console.hub.entity.spark.request;

import com.iflytek.astron.console.hub.entity.spark.Text;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Data
@Alias("SparkRequestMessage")
public class Message {
    List<Text> text;
}
