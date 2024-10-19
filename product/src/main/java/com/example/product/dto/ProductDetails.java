package com.example.product.dto;

import com.example.product.domain.Product;

public record ProductDetails(
        Long productId,
        String name,
        String price,
        int stockQuantity
) {
    public static ProductDetails from(Product product) {
        return new ProductDetails(
                product.getId(),
                product.getName(),
                product.getPrice().amount().toString(),
                product.getStockQuantity()
        );
    }
}
