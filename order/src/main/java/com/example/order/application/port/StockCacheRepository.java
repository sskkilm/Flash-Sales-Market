package com.example.order.application.port;

public interface StockCacheRepository {
    Long decreaseStock(Long productId, int quantity);

    Long increaseStock(Long productId, Integer quantity);
}
