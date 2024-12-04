package com.example.product.infrastructure.repository.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StockCacheRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String STOCK_CACHE_KEY = "products::%d:stock";

    public void save(Long productId, int stockQuantity) {
        String key = String.format(STOCK_CACHE_KEY, productId);

        Boolean isCached = redisTemplate.opsForValue().setIfAbsent(key, String.valueOf(stockQuantity));
        if (isCached) {
            log.info("{}번 상품 재고 캐싱 성공, 재고:{}", productId, stockQuantity);
        }
    }
}
