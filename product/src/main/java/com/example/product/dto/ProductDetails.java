package com.example.product.dto;

import com.example.product.domain.LimitedProduct;
import com.example.product.domain.NormalProduct;
import com.example.product.domain.Product;

public record ProductDetails<T>(T data) {

    public static ProductDetails<?> of(Product product) {
        if (product instanceof NormalProduct normalProduct) {
            return new ProductDetails<>(
                    NormalProductDetails.from(normalProduct)
            );
        }
        if (product instanceof LimitedProduct limitedProduct) {
            return new ProductDetails<>(
                    LimitedProductDetails.from(limitedProduct)
            );
        }
        throw new RuntimeException("Not supported product");
    }
}
