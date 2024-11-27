package com.example.member.application;

import com.example.member.application.port.TokenProvider;
import com.example.member.application.port.TokenRepository;
import com.example.member.common.dto.response.ReissueTokenResponse;
import com.example.member.domain.exception.TokenServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

import static com.example.member.domain.exception.ErrorCode.INVALID_TOKEN;
import static com.example.member.domain.exception.ErrorCode.TOKEN_UNMATCHED;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final long ACCESS_TOKEN_EXPIRED_MS = 1000 * 60 * 30L;
    private static final long REFRESH_TOKEN_EXPIRED_MS = 1000 * 60 * 60 * 24 * 7L;

    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;

    public String generateAccessToken(Long memberId, Date now) {
        return tokenProvider.generateToken(memberId, now, ACCESS_TOKEN_EXPIRED_MS);
    }

    public String generateRefreshToken(Long memberId, Date now) {
        return tokenProvider.generateToken(memberId, now, REFRESH_TOKEN_EXPIRED_MS);
    }

    public void saveRefreshToken(Long memberId, String refreshToken, Date now) {
        tokenRepository.saveRefreshToken(memberId, refreshToken, now, REFRESH_TOKEN_EXPIRED_MS);
    }

    public ReissueTokenResponse reissueToken(String refreshToken, Date now) {

        Long memberId = tokenProvider.getMemberIdFromToken(refreshToken);

        String storedRefreshToken = tokenRepository.findRefreshTokenByMemberId(memberId);
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new TokenServiceException(TOKEN_UNMATCHED);
        }

        String newAccessToken = tokenProvider.generateToken(memberId, now, ACCESS_TOKEN_EXPIRED_MS);
        String newRefreshToken = tokenProvider.generateToken(memberId, now, REFRESH_TOKEN_EXPIRED_MS);

        tokenRepository.saveRefreshToken(memberId, newRefreshToken, now, REFRESH_TOKEN_EXPIRED_MS);

        return new ReissueTokenResponse(newAccessToken, newRefreshToken);
    }

    public void logout(Long memberId, String accessToken) {

        Long memberIdFromToken = tokenProvider.getMemberIdFromToken(accessToken);
        if (tokenMemberIdUnmatched(memberId, memberIdFromToken)) {
            throw new TokenServiceException(INVALID_TOKEN);
        }

        Date expiration = tokenProvider.getExpiration(accessToken);
        tokenRepository.addBlackList(memberId, accessToken, expiration);

        tokenRepository.deleteRefreshToken(memberId);
    }

    private static boolean tokenMemberIdUnmatched(Long memberId, Long memberIdFromToken) {
        return !Objects.equals(memberId, memberIdFromToken);
    }
}
