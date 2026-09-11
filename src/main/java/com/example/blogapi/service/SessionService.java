package com.example.blogapi.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final StringRedisTemplate redisTemplate;

    private String buildSessionKey(Long userId){
        return "session:user:" + userId;
    }

    private String buildRefreshKey(Long userId){
        return "refresh:user:" + userId;
    }

    public void createSession(
            Long userId,
            String sessionId,
            String refreshToken
    ){
        redisTemplate.opsForValue().set(
                buildSessionKey(userId),
                sessionId,
                Duration.ofDays(7)
        );

        redisTemplate.opsForValue().set(
                buildRefreshKey(userId),
                refreshToken,
                Duration.ofDays(7)
        );
    }

    public String getSessionId(Long userId){
        return redisTemplate.opsForValue().get(buildSessionKey(userId));
    }

    public String getRefreshToken(Long userId){
        return redisTemplate.opsForValue().get(buildRefreshKey(userId));
    }

    public void deleteSessionId(Long userId){
        redisTemplate.delete(buildSessionKey(userId));
        redisTemplate.delete(buildRefreshKey(userId));
    }

    public boolean isValidSession(
            Long userId,
            String sessionId
    ){
        String currentSessionId = getSessionId(userId);

        return currentSessionId != null && currentSessionId.equals(sessionId);
    }
}
