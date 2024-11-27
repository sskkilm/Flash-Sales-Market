package com.example.member.infrastructure.repository;

import com.example.member.application.port.TokenRepository;
import com.example.member.infrastructure.repository.redis.TokenRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

    private final TokenRedisRepository tokenRedisRepository;

    @Override
    public void saveRefreshToken(Long memberId, String refreshToken, Date now, long expiredMs) {
        tokenRedisRepository.saveRefreshToken(memberId, refreshToken, now, expiredMs);
    }

    @Override
    public String findRefreshTokenByMemberId(Long memberId) {
        return tokenRedisRepository.findRefreshTokenByMemberId(memberId);
    }

    @Override
    public void deleteRefreshToken(Long memberId) {
        tokenRedisRepository.deleteRefreshToken(memberId);
    }

    @Override
    public void addBlackList(Long memberId, String accessToken, Date expiration) {
        tokenRedisRepository.addBlackList(memberId, accessToken, expiration);
    }
}
