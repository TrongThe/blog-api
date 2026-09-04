package com.example.blogapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryCreateRequest(
        @NotBlank(message = "Category name must not be blank")
        @Size(max = 100, message = "Category name must not exceed 100 characters")
        String name,

        @Size(
                max = 255,
                message = "Description must not exceed 255 characters"
        )
        String description
) {
}
