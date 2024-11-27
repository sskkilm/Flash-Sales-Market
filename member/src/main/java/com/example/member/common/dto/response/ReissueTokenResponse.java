package com.example.member.common.dto.response;

public record ReissueTokenResponse(
        String accessToken,
        String refreshToken
) {
}
