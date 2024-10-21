package com.example.product.domain;

import com.example.common.domain.Money;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Product {
    private Long id;
    private String name;
    private Money price;
    private int stockQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void decreaseStock(int quantity) {
        if (stockQuantity < quantity) {
            throw new IllegalArgumentException("product is out of stock");
        }

        this.stockQuantity -= quantity;
    }

}
