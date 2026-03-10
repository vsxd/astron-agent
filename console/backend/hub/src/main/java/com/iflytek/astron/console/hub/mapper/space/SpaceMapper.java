package com.iflytek.astron.console.hub.mapper.space;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.astron.console.hub.dto.space.EnterpriseSpaceCountVo;
import com.iflytek.astron.console.hub.dto.space.SpaceVo;
import com.iflytek.astron.console.hub.entity.space.Space;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SpaceMapper extends BaseMapper<Space> {

    List<SpaceVo> recentVisitList(@Param("uid") String uid, @Param("enterpriseId") Long enterpriseId);

    List<SpaceVo> joinList(@Param("uid") String uid, @Param("enterpriseId") Long enterpriseId,
            @Param("name") String name);

    List<SpaceVo> selfList(@Param("uid") String uid, @Param("role") Integer role,
            @Param("enterpriseId") Long enterpriseId, @Param("name") String name);

    List<SpaceVo> corporateList(@Param("uid") String uid, @Param("enterpriseId") Long enterpriseId,
            @Param("name") String name);

    SpaceVo getByUidAndId(@Param("uid") String uid, @Param("spaceId") Long spaceId);

    EnterpriseSpaceCountVo corporateCount(@Param("uid") String uid, @Param("enterpriseId") Long enterpriseId);

}
