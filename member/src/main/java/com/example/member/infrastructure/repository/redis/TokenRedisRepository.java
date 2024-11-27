package com.example.member.infrastructure.repository.redis;

import com.example.member.domain.exception.TokenServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;

import static com.example.member.domain.exception.ErrorCode.TOKEN_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class TokenRedisRepository {

    private static final String REFRESH_TOKEN_KEY = "members:%d:refresh_token";
    private static final String BLACK_LIST_KEY = "members:%d:black_list";

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveRefreshToken(Long memberId, String refreshToken, Date now, long expiredMs) {

        String key = String.format(REFRESH_TOKEN_KEY, memberId);

        redisTemplate.opsForValue().set(key, refreshToken);
        redisTemplate.expireAt(key, new Date(now.getTime() + expiredMs));
    }

    public String findRefreshTokenByMemberId(Long memberId) {

        String key = String.format(REFRESH_TOKEN_KEY, memberId);

        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            throw new TokenServiceException(TOKEN_NOT_FOUND);
        }

        return (String) value;
    }

    public void deleteRefreshToken(Long memberId) {

        String key = String.format(REFRESH_TOKEN_KEY, memberId);

        redisTemplate.delete(key);
    }

    public void addBlackList(Long memberId, String accessToken, Date expiration) {

        String key = String.format(BLACK_LIST_KEY, memberId);

        redisTemplate.opsForValue().set(key, accessToken);
        redisTemplate.expireAt(key, expiration);
    }
}
