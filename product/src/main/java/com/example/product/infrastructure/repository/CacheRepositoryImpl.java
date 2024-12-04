package com.example.product.infrastructure.repository;

import com.example.product.application.port.CacheRepository;
import com.example.product.infrastructure.repository.cache.StockCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CacheRepositoryImpl implements CacheRepository {

    private final StockCacheRepository stockCacheRepository;

    @Override
    public void saveStock(Long productId, int stockQuantity) {
        stockCacheRepository.save(productId, stockQuantity);
    }
}
