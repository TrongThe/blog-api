package com.example.blogapi.dto.response;


import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        String email,
        String role,
        boolean enabled,
        LocalDateTime createAt
) {
}
