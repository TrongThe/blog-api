package com.example.blogapi.dto.response;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
}
