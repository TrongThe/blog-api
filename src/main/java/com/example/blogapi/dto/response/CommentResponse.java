package com.example.blogapi.dto.response;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        String username,
        Long postId,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
}
