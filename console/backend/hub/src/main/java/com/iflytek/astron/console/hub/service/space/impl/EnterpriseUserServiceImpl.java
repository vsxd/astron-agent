package com.iflytek.astron.console.hub.service.space.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.astron.console.hub.data.UserInfoDataService;
import com.iflytek.astron.console.hub.dto.space.EnterpriseUserParam;
import com.iflytek.astron.console.hub.entity.space.EnterpriseUser;
import com.iflytek.astron.console.hub.entity.user.UserInfo;
import com.iflytek.astron.console.hub.enums.space.EnterpriseRoleEnum;
import com.iflytek.astron.console.hub.mapper.space.EnterpriseUserMapper;
import com.iflytek.astron.console.hub.service.space.EnterpriseUserService;
import com.iflytek.astron.console.commons.util.space.EnterpriseInfoUtil;
import com.iflytek.astron.console.hub.dto.space.EnterpriseUserVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * Enterprise team users
 */
@Service
@RequiredArgsConstructor
public class EnterpriseUserServiceImpl extends ServiceImpl<EnterpriseUserMapper, EnterpriseUser> implements EnterpriseUserService {
    private final UserInfoDataService userInfoDataService;

    @Override
    public EnterpriseUser getEnterpriseUserByUid(Long enterpriseId, String uid) {
        return baseMapper.selectByUidAndEnterpriseId(uid, enterpriseId);
    }

    @Override
    public Long countByEnterpriseIdAndUids(Long enterpriseId, List<String> uids) {
        return this.baseMapper.selectCount(Wrappers.<EnterpriseUser>lambdaQuery()
                .eq(EnterpriseUser::getEnterpriseId, enterpriseId)
                .in(EnterpriseUser::getUid, uids));
    }

    @Override
    public List<EnterpriseUser> listByEnterpriseId(Long enterpriseId) {
        return baseMapper.selectList(Wrappers.<EnterpriseUser>lambdaQuery()
                .eq(EnterpriseUser::getEnterpriseId, enterpriseId));
    }

    @Override
    @Transactional
    public boolean addEnterpriseUser(Long enterpriseId, String uid, EnterpriseRoleEnum roleEnum) {
        // Check whether the user already exists
        if (getEnterpriseUserByUid(enterpriseId, uid) != null) {
            return true;
        }
        UserInfo userInfo = userInfoDataService.findByUid(uid).orElseThrow();
        return this.save(EnterpriseUser.builder()
                .enterpriseId(enterpriseId)
                .uid(uid)
                .nickname(userInfo.getNickname())
                .role(roleEnum.getCode())
                .build());
    }

    @Override
    public List<EnterpriseUser> listByRole(Long enterpriseId, EnterpriseRoleEnum roleEnum) {
        return this.baseMapper.selectList(Wrappers.<EnterpriseUser>lambdaQuery()
                .eq(EnterpriseUser::getRole, roleEnum.getCode())
                .eq(EnterpriseUser::getEnterpriseId, enterpriseId));
    }

    @Override
    public Long countByEnterpriseId(Long enterpriseId) {
        return this.baseMapper.selectCount(Wrappers.<EnterpriseUser>lambdaQuery()
                .eq(EnterpriseUser::getEnterpriseId, enterpriseId));
    }

    @Override
    public Page<EnterpriseUserVo> page(EnterpriseUserParam param) {
        Page<EnterpriseUser> page = new Page<>();
        page.setSize(param.getPageSize());
        page.setCurrent(param.getPageNum());
        Long enterpriseId = EnterpriseInfoUtil.getEnterpriseId();
        if (enterpriseId == null) {
            return Page.of(param.getPageNum(), param.getPageSize());
        }
        Page<EnterpriseUserVo> result = this.baseMapper.selectVoPageByParam(page, enterpriseId, param.getNickname(), param.getRole());
        for (EnterpriseUserVo vo : result.getRecords()) {
            UserInfo userInfo = userInfoDataService.findByUid(vo.getUid()).orElseThrow();
            vo.setUsername(userInfo.getUsername());
            vo.setNickname(userInfo.getNickname());
        }
        return result;
    }

    @Override
    public boolean removeById(EnterpriseUser entity) {
        return super.removeById(entity);
    }

    @Override
    public boolean updateById(EnterpriseUser entity) {
        return super.updateById(entity);
    }
}
