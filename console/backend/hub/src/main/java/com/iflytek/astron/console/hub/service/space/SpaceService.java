package com.iflytek.astron.console.hub.service.space;


import com.iflytek.astron.console.hub.dto.space.EnterpriseSpaceCountVo;
import com.iflytek.astron.console.hub.dto.space.SpaceVo;
import com.iflytek.astron.console.hub.entity.space.Space;
import com.iflytek.astron.console.hub.enums.space.SpaceTypeEnum;

import java.util.List;

/**
 * Space
 */
public interface SpaceService {

    List<SpaceVo> recentVisitList();

    List<SpaceVo> personalList(String name);

    List<SpaceVo> personalSelfList(String name);

    List<SpaceVo> corporateJoinList(String name);

    List<SpaceVo> corporateList(String name);

    EnterpriseSpaceCountVo corporateCount();

    SpaceVo getSpaceVo();

    void setLastVisitPersonalSpaceTime();

    SpaceVo getLastVisitSpace();

    Long countByEnterpriseId(Long enterpriseId);

    Long countByUid(String uid);

    Space getSpaceById(Long id);

    List<SpaceVo> listByEnterpriseIdAndUid(Long enterpriseId, String uid);

    boolean checkExistByName(String name, Long id);

    SpaceTypeEnum getSpaceType(Long spaceId);

    boolean save(Space space);

    Space getById(Long id);

    boolean removeById(Long id);

    boolean updateById(Space space);

}
