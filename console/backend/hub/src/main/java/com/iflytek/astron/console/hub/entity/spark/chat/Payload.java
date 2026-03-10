package com.iflytek.astron.console.hub.entity.spark.chat;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("SparkChatPayload")
public class Payload {
    @JSONField(name = "chat_id")
    String chatId;

    Message message;

    Object extra;

}
