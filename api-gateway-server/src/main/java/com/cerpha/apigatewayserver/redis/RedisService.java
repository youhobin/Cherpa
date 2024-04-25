package com.cerpha.apigatewayserver.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean hasKeyBlackList(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}
