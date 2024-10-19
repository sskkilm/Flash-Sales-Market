package com.example.product.infrastructure;

import com.example.product.domain.Money;
import com.example.product.domain.Product;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity(name = "Product")
@EntityListeners(AuditingEntityListener.class)
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private int stockQuantity;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Product toModel() {
        return Product.builder()
                .id(this.id)
                .name(this.name)
                .price(Money.of(this.price))
                .stockQuantity(this.stockQuantity)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
