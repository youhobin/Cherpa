package com.cerpha.cerphaproject.common.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRefreshToken(String userId, String refreshToken) {
        redisTemplate.opsForValue().set(userId, refreshToken, Duration.ofDays(1));
    }

    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get(userId);
    }

    public void saveEmailAuthNumber(String email, int authNumber) {
        redisTemplate.opsForValue().set(email, String.valueOf(authNumber), Duration.ofMinutes(10));
    }

    public String getEmailInfo(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    public void saveIsEmailVerified(String email, boolean isVerified) {
        redisTemplate.opsForValue().set(email, String.valueOf(isVerified), Duration.ofMinutes(10));
    }
}
