package com.example.order.infrastructure.repository.cache;

import com.example.order.domain.exception.OrderServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.util.Collections;

import static com.example.order.domain.exception.ErrorCode.INSUFFICIENT_STOCK;

@Repository
@RequiredArgsConstructor
public class StockCacheRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String STOCK_CACHE_KEY = "products::%d:stock";

    public Long decreaseStock(Long productId, int quantity) {
        String script =
                "local current = tonumber(redis.call('GET', KEYS[1])) " +
                        "if current - ARGV[1] < 0 then " +
                        "  return -1 " +
                        "else " +
                        "  return redis.call('DECRBY', KEYS[1], ARGV[1]) " +
                        "end";

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);

        String key = String.format(STOCK_CACHE_KEY, productId);
        Long result = redisTemplate.execute(redisScript, Collections.singletonList(key), String.valueOf(quantity));
        if (result != null && result == -1L) {
            throw new OrderServiceException(INSUFFICIENT_STOCK);
        }
        return result;
    }

    public Long increaseStock(Long productId, Integer quantity) {
        String key = String.format(STOCK_CACHE_KEY, productId);
        return redisTemplate.opsForValue().increment(key, quantity);
    }
}
