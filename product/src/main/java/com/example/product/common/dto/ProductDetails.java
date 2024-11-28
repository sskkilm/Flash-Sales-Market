package com.example.product.common.dto;

import com.example.product.domain.EventProduct;
import com.example.product.domain.NormalProduct;
import com.example.product.domain.Product;

public record ProductDetails<T>(T data) {

    public static ProductDetails<?> of(Product product) {
        if (product instanceof NormalProduct normalProduct) {
            return new ProductDetails<>(
                    NormalProductDetails.from(normalProduct)
            );
        }
        if (product instanceof EventProduct eventProduct) {
            return new ProductDetails<>(
                    EventProductDetails.from(eventProduct)
            );
        }
        throw new RuntimeException("Not supported product");
    }
}
