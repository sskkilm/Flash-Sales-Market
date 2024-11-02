package com.example.product.domain;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class LimitedProduct extends Product {

    private LocalDateTime openTime;

    @Override
    public ProductType getType() {
        return ProductType.LIMITED;
    }
}
