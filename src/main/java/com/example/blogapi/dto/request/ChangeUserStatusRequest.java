package com.example.blogapi.dto.request;

import jakarta.validation.constraints.NotNull;

public record ChangeUserStatusRequest(
        @NotNull(message = "{user.enable.required}")
        Boolean enabled
) {
}
