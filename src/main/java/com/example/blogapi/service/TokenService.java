package com.example.blogapi.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final StringRedisTemplate redisTemplate;

    private String buildAccessKey(Long userId){
        return "session:user:" + userId;
    }

    private String buildRefreshKey(Long userId){
        return "refresh:user:" + userId;
    }

    public void saveToken(
            Long userId,
            String sessionId,
            String refreshToken
    ){
        redisTemplate.opsForValue().set(
                buildAccessKey(userId),
                sessionId,
                Duration.ofDays(7)
        );

        redisTemplate.opsForValue().set(
                buildRefreshKey(userId),
                refreshToken,
                Duration.ofDays(7)
        );
    }

    public String getAccessToken(Long userId){
        return redisTemplate.opsForValue().get(buildAccessKey(userId));
    }

    public String getRefreshToken(Long userId){
        return redisTemplate.opsForValue().get(buildRefreshKey(userId));
    }

    public void deleteTokens(Long userId){
        redisTemplate.delete(buildAccessKey(userId));
        redisTemplate.delete(buildRefreshKey(userId));
    }

    public boolean isValidAccessToken(
            Long userId,
            String accessToken
    ){
        String storedToken = getAccessToken(userId);

        return storedToken != null && storedToken.equals(accessToken);
    }
}
