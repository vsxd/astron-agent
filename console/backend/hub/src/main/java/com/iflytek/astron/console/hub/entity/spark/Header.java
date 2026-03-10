package com.iflytek.astron.console.hub.entity.spark;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("SparkHeader")
public class Header {
    // request
    @JSONField(name = "app_id")
    String appId;

    String uid;

    // response
    Integer code;
    String message;
    String sid;
    Integer status;
}
