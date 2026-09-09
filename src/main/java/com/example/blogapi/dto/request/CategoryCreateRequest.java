package com.example.blogapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryCreateRequest(
        @NotBlank(message = "{category.name.required}")
        @Size(max = 100, message = "{category.name.max}")
        String name,

        @Size(
                max = 255,
                message = "{category.description.max}"
        )
        String description
) {
}
