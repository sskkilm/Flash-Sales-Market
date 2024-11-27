package com.example.member.common.dto.request;

import jakarta.validation.constraints.NotNull;

public record LogoutRequest(
        @NotNull String accessToken
) {
}
