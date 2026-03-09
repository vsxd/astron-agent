package com.iflytek.astron.console.hub.service.bot;


import com.iflytek.astron.console.hub.dto.bot.BotFavoritePageDto;
import com.iflytek.astron.console.hub.dto.bot.BotMarketForm;

import java.util.List;

public interface BotFavoriteService {

    BotFavoritePageDto selectPage(BotMarketForm botMarketForm, String uid, String langCode);

    void create(String uid, Integer botId);

    void delete(String uid, Integer botId);

    int getFavoriteNumByBotId(Integer botId);

    List<Integer> list(String uid);
}
