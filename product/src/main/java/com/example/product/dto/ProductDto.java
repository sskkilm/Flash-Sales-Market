package com.example.product.dto;

import com.example.product.domain.Product;

public record ProductDto(
        Long productId,
        String name,
        String price
) {

    public static ProductDto from(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice().toString()
        );
    }
}
