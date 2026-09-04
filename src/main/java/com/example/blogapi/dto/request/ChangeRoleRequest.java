package com.example.blogapi.dto.request;

import com.example.blogapi.entity.Role;
import jakarta.validation.constraints.NotNull;

public record ChangeRoleRequest(
        @NotNull(message = "Role must not be null")
        Role role
) {
}
