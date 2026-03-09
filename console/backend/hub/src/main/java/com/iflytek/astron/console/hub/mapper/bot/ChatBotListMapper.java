package com.iflytek.astron.console.hub.mapper.bot;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.astron.console.hub.entity.bot.ChatBotBase;
import com.iflytek.astron.console.hub.entity.bot.ChatBotList;
import org.apache.ibatis.annotations.Mapper;

import java.util.LinkedList;
import java.util.Map;

@Mapper
public interface ChatBotListMapper extends BaseMapper<ChatBotList> {

    Long countCheckBotList(Map<String, Object> map);

    LinkedList<Map<String, Object>> getCheckBotList(Map<String, Object> map);

    void baseBotInsert(ChatBotBase botBase);

}
