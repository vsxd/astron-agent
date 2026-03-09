package com.iflytek.astron.console.hub.entity.spark;

import com.iflytek.astron.console.hub.entity.spark.request.FcFunction;
import com.iflytek.astron.console.hub.entity.spark.request.Message;
import com.iflytek.astron.console.hub.entity.spark.response.Choices;
import com.iflytek.astron.console.hub.entity.spark.response.Usage;
import lombok.Data;

@Data
public class Payload {
    // request
    Message message;

    // response
    Choices choices;
    Usage usage;

    FcFunction functions;
}
