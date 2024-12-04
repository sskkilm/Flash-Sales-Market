package com.example.order.infrastructure.repository;

import com.example.order.application.port.CacheRepository;
import com.example.order.infrastructure.repository.cache.StockCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CacheRepositoryImpl implements CacheRepository {

    private final StockCacheRepository stockCacheRepository;

    @Override
    public Long decreaseStock(Long productId, int quantity) {
        return stockCacheRepository.decreaseStock(productId, quantity);
    }

    @Override
    public Long increaseStock(Long productId, Integer quantity) {
        return stockCacheRepository.increaseStock(productId, quantity);
    }
}
