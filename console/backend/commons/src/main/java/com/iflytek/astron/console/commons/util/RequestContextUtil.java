package com.iflytek.astron.console.commons.util;

import com.iflytek.astron.console.commons.constant.ResponseEnum;
import com.iflytek.astron.console.commons.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public final class RequestContextUtil {

    public static final String USER_ID_ATTRIBUTE = "X-User-Id";
    public static final String USER_INFO_ATTRIBUTE = "X-User-Info";

    private RequestContextUtil() {}

    public static String getUID() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            throw new BusinessException(ResponseEnum.UNAUTHORIZED);
        }
        String uid = (String) request.getAttribute(USER_ID_ATTRIBUTE);
        if (StringUtils.isBlank(uid)) {
            throw new BusinessException(ResponseEnum.UNAUTHORIZED);
        }
        return uid;
    }

    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
}
