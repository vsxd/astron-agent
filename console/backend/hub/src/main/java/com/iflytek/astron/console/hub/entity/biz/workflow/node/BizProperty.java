package com.iflytek.astron.console.hub.entity.biz.workflow.node;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Data
@Alias("BizWorkflowBizProperty")
public class BizProperty {
    String id;
    String name;
    @JSONField(name = "default")
    @JsonProperty("default")
    String dft;
    Boolean required;
    String type;
    List<BizProperty> properties;
}
