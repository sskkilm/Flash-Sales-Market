package com.example.product.infrastructure.repository.persistence.mapper;

import com.example.product.domain.NormalProduct;
import com.example.product.domain.Product;
import com.example.product.infrastructure.repository.persistence.entity.NormalProductEntity;
import com.example.product.infrastructure.repository.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class NormalProductMapHandler implements ProductMapHandler {
    @Override
    public boolean support(Product product) {
        return product instanceof NormalProduct;
    }

    @Override
    public ProductEntity handle(Product product) {
        NormalProduct normalProduct = (NormalProduct) product;
        return NormalProductEntity.builder()
                .id(normalProduct.getId())
                .name(normalProduct.getName())
                .price(normalProduct.getPrice())
                .stockQuantity(normalProduct.getStockQuantity())
                .createdAt(normalProduct.getCreatedAt())
                .updatedAt(normalProduct.getUpdatedAt())
                .build();
    }
}
