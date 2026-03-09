package com.iflytek.astron.console.hub.service.space;

import com.iflytek.astron.console.hub.entity.space.SpacePermission;

import java.util.Collection;
import java.util.List;

/**
 * Space role permission configuration
 */
public interface SpacePermissionService {

    SpacePermission getSpacePermissionByKey(String key);

    List<String> listByKeys(Collection<String> keys);

    void insertBatch(List<SpacePermission> spacePermissions);

}
