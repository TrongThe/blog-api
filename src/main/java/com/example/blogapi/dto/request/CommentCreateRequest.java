package com.example.blogapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateRequest(

        @NotBlank(message = "{comment.content.required}")
        @Size(
                max = 1000,
                message = "{comment.content.max}"
        )
        String content
) {
}
