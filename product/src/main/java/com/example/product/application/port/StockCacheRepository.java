package com.example.product.application.port;

public interface StockCacheRepository {
    void cache(Long productId, int stockQuantity);
}
