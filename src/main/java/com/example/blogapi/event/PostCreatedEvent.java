package com.example.blogapi.event;

public record PostCreatedEvent(
        Long postId,
        String title,
        Long authorId,
        String authorUsername
) {
}
