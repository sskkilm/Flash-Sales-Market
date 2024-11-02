package com.example.product.infrastructure.entity;

import com.example.product.domain.LimitedProduct;
import com.example.product.domain.Product;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Entity
@DiscriminatorValue("LIMITED")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class LimitedProductEntity extends ProductEntity {

    private LocalDateTime openTime;

    @Override
    public Product toModel() {
        return LimitedProduct.builder()
                .id(this.getId())
                .name(this.getName())
                .price(this.getPrice())
                .stockQuantity(this.getStockQuantity())
                .openTime(this.getOpenTime())
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}
