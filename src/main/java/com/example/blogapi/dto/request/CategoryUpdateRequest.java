package com.example.blogapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryUpdateRequest(
        @NotBlank(message = "{category.name.required}")
        @Size(
                max = 50,
                message = "{category.name.max}"
        )
        String name,

        @Size(
                max = 255,
                message = "{category.description.max}"
        )
        String description

) {
}
