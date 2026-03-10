package com.iflytek.astron.console.hub.entity.spark.chat;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("SparkChatHeader")
public class Header {
    Integer code;
    String message;
    String sid;
    Integer status;
    Integer seq;
    @JSONField(name = "is_finish")
    Boolean isFinish;
}
