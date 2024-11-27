package com.example.member.application.port;

import java.util.Date;

public interface TokenProvider {

    String generateToken(Long memberId, Date now, long expiredMs);

    Long getMemberIdFromToken(String token);

    Date getExpiration(String token);
}
