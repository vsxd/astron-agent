package com.iflytek.astron.console.hub.service.user.impl;

import com.iflytek.astron.console.commons.constant.ResponseEnum;
import com.iflytek.astron.console.commons.exception.BusinessException;
import com.iflytek.astron.console.commons.response.ApiResult;
import com.iflytek.astron.console.hub.service.user.MessageCodeService;
import org.springframework.stereotype.Service;

/**
 * Default no-op implementation of MessageCodeService.
 * SMS verification is only available in the commercial edition.
 */
@Service
public class DefaultMessageCodeServiceImpl implements MessageCodeService {

    @Override
    public ApiResult<Void> sendLoginMessageCode(String mobile) {
        throw new BusinessException(ResponseEnum.TOOLKIT_UNSUPPORTED_OPERATION);
    }

    @Override
    public void checkLoginMessageCode(String mobile, String verifyCode) {
        throw new BusinessException(ResponseEnum.TOOLKIT_UNSUPPORTED_OPERATION);
    }

    @Override
    public void sendVerifyCodeCommon(String mobile, String prefix) {
        throw new BusinessException(ResponseEnum.TOOLKIT_UNSUPPORTED_OPERATION);
    }

    @Override
    public void checkVerifyCodeCommon(String mobile, String verifyCode, String prefix) {
        throw new BusinessException(ResponseEnum.TOOLKIT_UNSUPPORTED_OPERATION);
    }
}
