package com.iflytek.astron.console.hub.mapper.space;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.astron.console.hub.dto.space.EnterpriseVo;
import com.iflytek.astron.console.hub.entity.space.Enterprise;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EnterpriseMapper extends BaseMapper<Enterprise> {
    List<EnterpriseVo> selectByJoinUid(String joinUid);
}
