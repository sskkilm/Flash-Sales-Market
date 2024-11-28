package com.example.product.infrastructure.repository.persistence.entity;

import com.example.product.domain.EventProduct;
import com.example.product.domain.Product;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Entity(name = "EventProduct")
@DiscriminatorValue("EVENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class EventProductEntity extends ProductEntity {

    private LocalDateTime openTime;

    @Override
    public Product toModel() {
        return EventProduct.builder()
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
