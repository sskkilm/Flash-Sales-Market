package com.example.product.infrastructure.repository.mapper;

import com.example.product.domain.LimitedProduct;
import com.example.product.domain.Product;
import com.example.product.infrastructure.entity.LimitedProductEntity;
import com.example.product.infrastructure.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class LimitedProductMapHandler implements ProductMapHandler {
    @Override
    public boolean support(Product product) {
        return product instanceof LimitedProduct;
    }

    @Override
    public ProductEntity handle(Product product) {
        LimitedProduct limitedProduct = (LimitedProduct) product;
        return LimitedProductEntity.builder()
                .id(limitedProduct.getId())
                .name(limitedProduct.getName())
                .price(limitedProduct.getPrice())
                .stockQuantity(limitedProduct.getStockQuantity())
                .openTime(limitedProduct.getOpenTime())
                .createdAt(limitedProduct.getCreatedAt())
                .updatedAt(limitedProduct.getUpdatedAt())
                .build();
    }
}
