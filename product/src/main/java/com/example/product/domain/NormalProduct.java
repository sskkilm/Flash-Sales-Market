package com.example.product.domain;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class NormalProduct extends Product {

    @Override
    public ProductType getType() {
        return ProductType.NORMAL;
    }
}
