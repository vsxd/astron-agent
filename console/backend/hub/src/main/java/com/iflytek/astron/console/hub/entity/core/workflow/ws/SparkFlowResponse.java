package com.iflytek.astron.console.hub.entity.core.workflow.ws;


import com.iflytek.astron.console.hub.entity.spark.Parameter;
import com.iflytek.astron.console.hub.entity.spark.chat.Payload;
import lombok.Data;

@Data
public class SparkFlowResponse {
    SparkFlowResponseHeader header;
    Payload payload;
    Parameter parameter;
}
