package com.example.product.domain;

import com.example.product.exception.InsufficientStockException;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@SuperBuilder
public abstract class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private int stockQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public abstract ProductType getType();

    public void checkOutOfStock(int quantity) {
        if (this.stockQuantity < quantity) {
            throw new InsufficientStockException(
                    "상품 재고가 부족합니다. 상품 id: " + this.id + ", 남은 재고 수량: " + stockQuantity
            );
        }
    }

    public void decreaseStock(int quantity) {
        checkOutOfStock(quantity);

        this.stockQuantity -= quantity;
    }

    public void increaseStock(int quantity) {
        this.stockQuantity += quantity;
    }

}
