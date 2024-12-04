package com.example.product.application;

import com.example.product.application.port.CacheRepository;
import com.example.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final CacheRepository cacheRepository;

    public void saveStock(List<Product> products) {
        products.forEach(
                        product -> cacheRepository.saveStock(
                                product.getId(), product.getStockQuantity()
                        )
                );
    }
}
