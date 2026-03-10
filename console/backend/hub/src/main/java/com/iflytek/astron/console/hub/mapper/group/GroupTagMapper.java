package com.iflytek.astron.console.hub.mapper.group;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.astron.console.hub.entity.table.group.GroupTag;
import com.iflytek.astron.console.hub.entity.vo.group.GroupTagVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper

public interface GroupTagMapper extends BaseMapper<GroupTag> {
    List<GroupTagVo> listGroupTagVoByUid(@Param("uid") String uid);
}
