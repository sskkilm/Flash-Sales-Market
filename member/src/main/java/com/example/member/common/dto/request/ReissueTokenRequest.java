package com.example.member.common.dto.request;

import jakarta.validation.constraints.NotNull;

public record ReissueTokenRequest(
        @NotNull String refreshToken
) {
}
