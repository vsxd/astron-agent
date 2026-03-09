package com.iflytek.astron.console.hub.service.chat;


import com.iflytek.astron.console.hub.dto.chat.ChatListCreateResponse;

public interface ChatRestartService {
    ChatListCreateResponse createNewTreeIndexByRootChatId(Long chatId, String uid, String chatListName);
}
