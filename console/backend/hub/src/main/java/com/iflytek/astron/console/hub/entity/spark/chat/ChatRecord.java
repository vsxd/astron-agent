package com.iflytek.astron.console.hub.entity.spark.chat;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChatRecord {
    String role;
    String content;
}
