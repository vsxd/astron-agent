package com.iflytek.astron.console.hub.entity.spark;

import com.iflytek.astron.console.hub.entity.spark.request.Chat;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("SparkParameter")
public class Parameter {
    // request
    Chat chat;

    // response

}
