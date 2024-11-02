package com.example.product.infrastructure.entity;

import com.example.product.domain.NormalProduct;
import com.example.product.domain.Product;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("NORMAL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class NormalProductEntity extends ProductEntity {

    @Override
    public Product toModel() {
        return NormalProduct.builder()
                .id(this.getId())
                .name(this.getName())
                .price(this.getPrice())
                .stockQuantity(this.getStockQuantity())
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }

}
