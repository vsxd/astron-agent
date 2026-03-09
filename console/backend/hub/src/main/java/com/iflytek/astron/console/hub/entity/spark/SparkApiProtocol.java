package com.iflytek.astron.console.hub.entity.spark;


import lombok.Data;

@Data
public class SparkApiProtocol {
    Header header;
    Parameter parameter;
    Payload payload;
}
