package com.example.blogapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record PostUpdateRequest(
        @NotBlank(message = "{post.title.required}")
        @Size(max = 200, message = "{post.title.max}")
        String title,

        @NotBlank(message = "{post.content.required}")
        String content,

        @NotEmpty(message = "{post.category.required}")
        Set<Long> categoryIds
) {
}
