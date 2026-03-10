package com.iflytek.astron.console.hub.config.space;


import com.iflytek.astron.console.hub.service.space.EnterpriseSpaceService;
import com.iflytek.astron.console.commons.util.space.EnterpriseInfoUtil;
import com.iflytek.astron.console.hub.util.space.SpaceInfoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.RequiredArgsConstructor;

import jakarta.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class EnterpriseSpaceConfig {

    @Value("${space.header.id-key:space-id}")
    private String spaceIdKey;
    @Value("${enterprise.header.id-key:enterprise-id}")
    private String enterpriseIdKey;

    private final EnterpriseSpaceService enterpriseSpaceService;


    @PostConstruct
    public void init() {
        SpaceInfoUtil.init(enterpriseSpaceService, spaceIdKey);
        EnterpriseInfoUtil.init(enterpriseIdKey);
    }

}
