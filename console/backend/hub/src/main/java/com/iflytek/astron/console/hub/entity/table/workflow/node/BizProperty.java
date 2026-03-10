package com.iflytek.astron.console.hub.entity.table.workflow.node;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Data
@Alias("TableWorkflowBizProperty")
public class BizProperty {
    String id;
    String name;
    @JSONField(name = "default")
    String dft;
    Boolean required;
    String type;
    List<BizProperty> properties;
}
