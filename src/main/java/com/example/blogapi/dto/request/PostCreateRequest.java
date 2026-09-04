package com.example.blogapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record PostCreateRequest(
        @NotBlank(message = "Title must not be blank")
        @Size(max = 200, message = "Title must not exceed 200 character")
        String title,

        @NotBlank(message = "Content must be not blank")
        String content,

        @NotEmpty(message = "Post must have at least one category")
        Set<Long> categoryIds
) {
}
