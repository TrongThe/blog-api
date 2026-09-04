package com.example.blogapi.dto.response;

import com.example.blogapi.entity.PostStatus;

import java.time.LocalDateTime;
import java.util.Set;

public record PostResponse(
        Long id,
        String title,
        String content,
        PostStatus status,
        Long authorId,
        String authorUsername,
        Set<CategoryResponse> categories,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
