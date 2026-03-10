package com.iflytek.astron.console.hub.mapper.space;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iflytek.astron.console.hub.dto.space.InviteRecordVo;
import com.iflytek.astron.console.hub.entity.space.InviteRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InviteRecordMapper extends BaseMapper<InviteRecord> {

    InviteRecordVo selectVoById(Long id);

    Page<InviteRecordVo> selectVoPageByParam(Page<InviteRecord> page,
            @Param("type") Integer type,
            @Param("spaceId") Long spaceId,
            @Param("enterpriseId") Long enterpriseId,
            @Param("nickname") String nickname,
            @Param("status") Integer status);

    Long countJoiningByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

    Long countJoiningBySpaceId(@Param("spaceId") Long spaceId);

    Long countJoiningByUid(@Param("uid") String uid, @Param("spaceType") Integer spaceType);

}
