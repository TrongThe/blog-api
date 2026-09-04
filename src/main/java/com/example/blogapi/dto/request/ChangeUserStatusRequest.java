package com.example.blogapi.dto.request;

import jakarta.validation.constraints.NotNull;

public record ChangeUserStatusRequest(
        @NotNull(message = "Enabled must not be null")
        Boolean enabled
) {
}
