package com.example.member.infrastructure.jwt;

import com.example.member.application.port.TokenProvider;
import com.example.member.domain.exception.TokenServiceException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static com.example.member.domain.exception.ErrorCode.INVALID_TOKEN;
import static com.example.member.domain.exception.ErrorCode.TOKEN_EXPIRED;

@Component
public class JwtProvider implements TokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public String generateToken(Long memberId, Date now, long expiredMs) {

        Date expiredAt = new Date(now.getTime() + expiredMs);

        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        return BEARER_PREFIX + Jwts.builder()
                .subject(memberId.toString())
                .issuedAt(now)
                .expiration(expiredAt)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public Long getMemberIdFromToken(String token) {
        Claims claims = parseClaims(removePrefix(token));
        return Long.parseLong(claims.getSubject());
    }

    @Override
    public Date getExpiration(String token) {
        Claims claims = parseClaims(removePrefix(token));
        return claims.getExpiration();
    }

    private Claims parseClaims(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new TokenServiceException(TOKEN_EXPIRED, e);
        } catch (JwtException e) {
            throw new TokenServiceException(INVALID_TOKEN, e);
        }
    }

    private String removePrefix(String token) {
        return token.substring(BEARER_PREFIX.length());
    }
}
