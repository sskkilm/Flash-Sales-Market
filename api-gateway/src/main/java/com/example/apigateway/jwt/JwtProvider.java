package com.example.apigateway.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    public String getMemberIdFromToken(String token) {
        Claims claims = parseClaims(token);
        if (claims == null) {
            return null;
        }

        log.debug("JWT claims: {}", claims);
        log.debug("JWT subject: {}", claims.getSubject());
        return claims.getSubject();
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
            log.info("토큰 만료");
            return null;
        } catch (JwtException e) {
            log.info("유효하지 않은 토큰");
            return null;
        }
    }
}
