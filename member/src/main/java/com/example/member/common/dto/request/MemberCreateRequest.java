package com.example.member.common.dto.request;

import jakarta.validation.constraints.NotNull;

public record MemberCreateRequest(
        @NotNull String username,
        @NotNull String password
) {
}
