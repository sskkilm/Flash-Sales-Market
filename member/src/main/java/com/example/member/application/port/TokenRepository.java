package com.example.member.application.port;

import java.util.Date;

public interface TokenRepository {

    void saveRefreshToken(Long memberId, String refreshToken, Date now, long expiredMs);

    String findRefreshTokenByMemberId(Long memberId);

    void deleteRefreshToken(Long memberId);

    void addBlackList(Long memberId, String accessToken, Date expiration);
}
