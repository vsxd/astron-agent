package com.iflytek.astron.console.hub.mapper.space;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iflytek.astron.console.hub.entity.space.EnterpriseUser;
import com.iflytek.astron.console.hub.dto.space.EnterpriseUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EnterpriseUserMapper extends BaseMapper<EnterpriseUser> {

    EnterpriseUser selectByUidAndEnterpriseId(String uid, Long enterpriseId);

    Page<EnterpriseUserVO> selectVOPageByParam(Page<EnterpriseUser> page,
            @Param("enterpriseId") Long enterpriseId,
            @Param("nickname") String nickname,
            @Param("role") Integer role);

}
