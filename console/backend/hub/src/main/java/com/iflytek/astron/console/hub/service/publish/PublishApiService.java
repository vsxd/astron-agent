package com.iflytek.astron.console.hub.service.publish;

import com.iflytek.astron.console.hub.dto.publish.AppListDto;
import com.iflytek.astron.console.hub.dto.publish.BotApiInfoDto;
import com.iflytek.astron.console.hub.dto.publish.CreateAppVo;
import com.iflytek.astron.console.hub.dto.publish.CreateBotApiVo;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author yun-zhi-ztl
 */
public interface PublishApiService {
    Boolean createApp(CreateAppVo createAppVo);

    List<AppListDto> getAppList();

    BotApiInfoDto createBotApi(CreateBotApiVo createBotApiVo, HttpServletRequest request);

    BotApiInfoDto getApiInfo(Long botId);
}
