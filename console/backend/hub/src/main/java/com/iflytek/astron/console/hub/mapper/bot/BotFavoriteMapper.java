package com.iflytek.astron.console.hub.mapper.bot;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.astron.console.hub.dto.bot.BotFavoriteQueryDto;
import com.iflytek.astron.console.hub.entity.bot.BotFavorite;
import com.iflytek.astron.console.hub.dto.bot.ChatBotMarketPage;

import java.util.LinkedList;

public interface BotFavoriteMapper extends BaseMapper<BotFavorite> {

    LinkedList<ChatBotMarketPage> selectBotPage(BotFavoriteQueryDto queryDto);

    Long countBotPage(BotFavoriteQueryDto queryDto);

}
