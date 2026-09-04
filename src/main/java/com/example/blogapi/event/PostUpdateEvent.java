package com.example.blogapi.event;

public record PostUpdateEvent(
        Long postId,
        String title,
        Long authorId,
        String authorUsername
) {
}
