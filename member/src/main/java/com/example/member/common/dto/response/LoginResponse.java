package com.example.member.common.dto.response;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
}
