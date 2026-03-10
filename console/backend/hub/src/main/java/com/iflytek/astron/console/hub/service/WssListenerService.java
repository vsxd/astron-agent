package com.iflytek.astron.console.hub.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@Getter
@RequiredArgsConstructor
public class WssListenerService {

    private final ChatRecordModelService chatRecordModelService;

    private final RedissonClient redissonClient;

    public ChatRecordModelService getChatRecordModelService() {
        return chatRecordModelService;
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

}
