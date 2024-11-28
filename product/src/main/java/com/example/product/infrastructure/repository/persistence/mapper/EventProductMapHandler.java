package com.example.product.infrastructure.repository.persistence.mapper;

import com.example.product.domain.EventProduct;
import com.example.product.domain.Product;
import com.example.product.infrastructure.repository.persistence.entity.EventProductEntity;
import com.example.product.infrastructure.repository.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class EventProductMapHandler implements ProductMapHandler {
    @Override
    public boolean support(Product product) {
        return product instanceof EventProduct;
    }

    @Override
    public ProductEntity handle(Product product) {
        EventProduct eventProduct = (EventProduct) product;
        return EventProductEntity.builder()
                .id(eventProduct.getId())
                .name(eventProduct.getName())
                .price(eventProduct.getPrice())
                .stockQuantity(eventProduct.getStockQuantity())
                .openTime(eventProduct.getOpenTime())
                .createdAt(eventProduct.getCreatedAt())
                .updatedAt(eventProduct.getUpdatedAt())
                .build();
    }
}
