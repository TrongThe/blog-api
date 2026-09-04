package com.example.blogapi.dto.request;

import com.example.blogapi.entity.PostStatus;

public record PostSearchRequest(
        String title,
        Long categoryId,
        String author,
        PostStatus status
) {
}
