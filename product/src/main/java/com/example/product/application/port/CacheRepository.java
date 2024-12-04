package com.example.product.application.port;

public interface CacheRepository {
    void saveStock(Long productId, int stockQuantity);
}
