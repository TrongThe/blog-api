package com.example.blogapi.auth;

public record LoginResult(
        String accessToken,
        String refreshToken
) {
}
