package com.iflytek.astron.console.hub.service.user;

import com.iflytek.astron.console.hub.dto.user.MyBotPageDto;
import com.iflytek.astron.console.hub.dto.user.MyBotParamDto;


/**
 * @author wowo_zZ
 * @since 2025/9/9 19:23
 **/

public interface UserBotService {

    MyBotPageDto listMyBots(MyBotParamDto myBotParamDto);

    Boolean deleteBot(Integer botId);
}
