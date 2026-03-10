package com.iflytek.astron.console.hub.mapper.space;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iflytek.astron.console.hub.dto.space.ApplyRecordVo;
import com.iflytek.astron.console.hub.entity.space.ApplyRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ApplyRecordMapper extends BaseMapper<ApplyRecord> {
    Page<ApplyRecordVo> selectVoPageByParam(Page<ApplyRecord> page,
            @Param("spaceId") Long spaceId,
            @Param("enterpriseId") Long enterpriseId,
            @Param("nickname") String nickname,
            @Param("status") Integer status);
}
