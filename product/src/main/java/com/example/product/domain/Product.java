package com.example.product.domain;

import com.example.product.exception.ProductServiceException;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.example.product.exception.error.ErrorCode.INSUFFICIENT_STOCK;

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

    public void checkOutOfStock(int quantity, int holdStockQuantity) {
        if (this.stockQuantity - holdStockQuantity < quantity) {
            throw new ProductServiceException(INSUFFICIENT_STOCK);
        }
    }

    public void decreaseStock(int quantity) {
        if (this.stockQuantity < quantity) {
            throw new ProductServiceException(INSUFFICIENT_STOCK);
        }

        this.stockQuantity -= quantity;
    }

    public void increaseStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public boolean isLimited() {
        return this.getType().equals(ProductType.LIMITED);
    }

    public boolean isNotOpened() {
        return ((LimitedProduct) this).getOpenTime().isAfter(LocalDateTime.now());
    }
}
