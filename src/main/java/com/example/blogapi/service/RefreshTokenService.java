package com.example.blogapi.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final StringRedisTemplate redisTemplate;

    public void save(Long userId, String refreshToken, long expirationMs){

        redisTemplate.opsForValue().set(
                buildKey(userId),
                refreshToken,
                Duration.ofMillis(expirationMs)
        );
    }

    public String get(Long userId) {

        return redisTemplate.opsForValue()
                .get(buildKey(userId));
    }

    public void delete(Long userId) {

        redisTemplate.delete(buildKey(userId));
    }

    private String buildKey(Long userId) {
        return "refresh_token:" + userId;
    }
}
