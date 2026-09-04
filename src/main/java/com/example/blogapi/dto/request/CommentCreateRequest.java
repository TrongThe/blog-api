package com.example.blogapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateRequest(

        @NotBlank(message = "Comment content must not be blank")
        @Size(
                max = 1000,
                message = "Comment content must not exceed 1000 charasters"
        )
        String content
) {
}
