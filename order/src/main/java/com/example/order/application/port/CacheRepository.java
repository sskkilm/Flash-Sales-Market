package com.example.order.application.port;

public interface CacheRepository {
    Long decreaseStock(Long productId, int quantity);

    Long increaseStock(Long productId, Integer quantity);
}
