package com.iflytek.astron.console.hub.service.space;

import com.iflytek.astron.console.commons.response.ApiResult;
import com.iflytek.astron.console.hub.dto.space.SpaceAddDto;
import com.iflytek.astron.console.hub.dto.space.SpaceUpdateDto;
import com.iflytek.astron.console.hub.entity.space.Space;

public interface SpaceBizService {

    ApiResult<Long> create(SpaceAddDto spaceAddDto, Long enterpriseId);

    ApiResult<String> deleteSpace(Long spaceId, String mobile, String verifyCode);

    ApiResult<String> updateSpace(SpaceUpdateDto spaceUpdateDto);

    ApiResult<Space> visitSpace(Long spaceId);

    ApiResult<String> sendMessageCode(Long spaceId);

    ApiResult<Boolean> ossVersionUserUpgrade();
}
