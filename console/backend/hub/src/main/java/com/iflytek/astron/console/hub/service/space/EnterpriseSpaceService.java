package com.iflytek.astron.console.hub.service.space;

import com.iflytek.astron.console.hub.entity.space.EnterprisePermission;
import com.iflytek.astron.console.hub.entity.space.EnterpriseUser;
import com.iflytek.astron.console.hub.entity.space.SpacePermission;
import com.iflytek.astron.console.hub.entity.space.SpaceUser;

public interface EnterpriseSpaceService {

    String getUidByCurrentSpaceId(Long spaceId);

    SpaceUser checkUserBelongSpace(Long spaceId, String uid);

    void clearSpaceUserCache(Long spaceId, String uid);

    EnterpriseUser checkUserBelongEnterprise(Long enterpriseId, String uid);

    void clearEnterpriseUserCache(Long enterpriseId, String uid);

    EnterprisePermission getEnterprisePermissionByKey(String key);

    SpacePermission getSpacePermissionByKey(String key);

    boolean checkEnterpriseExpired(Long enterpriseId);

    boolean checkSpaceExpired(Long spaceId);

}
