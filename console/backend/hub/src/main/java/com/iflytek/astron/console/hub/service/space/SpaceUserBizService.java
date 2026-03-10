package com.iflytek.astron.console.hub.service.space;

import com.iflytek.astron.console.commons.response.ApiResult;
import com.iflytek.astron.console.hub.dto.space.UserLimitVo;

public interface SpaceUserBizService {

    ApiResult<String> enterpriseAdd(String uid, Integer role);

    ApiResult<String> remove(String uid);

    ApiResult<String> updateRole(String uid, Integer role);

    ApiResult<String> quitSpace();

    ApiResult<String> transferSpace(String uid);

    UserLimitVo getUserLimit();

    UserLimitVo getUserLimit(String uid);

    UserLimitVo getUserLimitVo(Integer type, String uid);
}
