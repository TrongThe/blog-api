package com.example.blogapi.event;

public record PostDeleteEvent(
        Long postId,
        Long authorId,
        String authorUsername
) {
}
