package com.iflytek.astron.console.hub.mapper.relation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.astron.console.hub.entity.dto.database.FlowDbRelCountDto;
import com.iflytek.astron.console.hub.entity.table.relation.FlowDbRel;

import java.util.List;

public interface FlowDbRelMapper extends BaseMapper<FlowDbRel> {


    List<FlowDbRelCountDto> selectCountsByDbIds(List<Long> dbIds);

    void insertBatch(List<FlowDbRel> dbRelList);
}
