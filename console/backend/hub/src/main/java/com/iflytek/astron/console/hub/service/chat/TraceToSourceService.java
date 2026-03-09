package com.iflytek.astron.console.hub.service.chat;

import com.iflytek.astron.console.hub.dto.chat.ChatRespModelDto;
import com.iflytek.astron.console.hub.entity.chat.ChatTraceSource;

import java.util.List;

public interface TraceToSourceService {

    void respAddTrace(List<ChatRespModelDto> respList, List<ChatTraceSource> traceList);
}
