package com.example.blogapi.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final StringRedisTemplate redisTemplate;
    private final JwtService jwtService;

    private static final String BLACKLIST_PREFIX = "blacklist:";
    private static final String REFRESH_PREFIX = "refresh:user:";

    private String buildRefreshKey(Long userId){
        return REFRESH_PREFIX + userId;
    }

    private String buildBlacklistKey(String jti){
        return BLACKLIST_PREFIX + jti;
    }

    public void saveRefreshToken(
            Long userId,
            String refreshToken
    ){
        redisTemplate.opsForValue().set(
                buildRefreshKey(userId),
                refreshToken,
                Duration.ofMillis(jwtService.getRefreshExpiration())
        );
    }

    public String getRefreshToken(Long userId){
        return redisTemplate.opsForValue().get(buildRefreshKey(userId));
    }

    public void blacklistAccessToken(String accessToken){

        String jti = jwtService.extractJti(accessToken);

        long remainingExpiration = jwtService.getRemainingExpiration(accessToken);

        if (remainingExpiration > 0){
            redisTemplate.opsForValue().set(
                    buildBlacklistKey(jti),
                    "revoked",
                    Duration.ofMillis(remainingExpiration)
            );
        }
    }

    public boolean isBlacklisted(String accessToken){

        String jti = jwtService.extractJti(accessToken);

        return Boolean.TRUE.equals(redisTemplate.hasKey(buildBlacklistKey(jti)));
    }

    public void deleteRefreshToken(Long userId){
        redisTemplate.delete(buildRefreshKey(userId));
    }
}
