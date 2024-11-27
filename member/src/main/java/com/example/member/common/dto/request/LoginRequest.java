package com.example.member.common.dto.request;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull String username,
        @NotNull String password
) {
}
