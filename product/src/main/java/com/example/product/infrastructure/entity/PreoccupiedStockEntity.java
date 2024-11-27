package com.example.product.infrastructure.entity;

import com.example.product.domain.PreoccupiedStock;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity(name = "PreoccupiedStock")
@Table(
        name = "preoccupied_stock",
        indexes = {@Index(name = "idx_product_id", columnList = "productId")}
)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PreoccupiedStockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long productId;

    private int quantity;

    @CreatedDate
    private LocalDateTime createAt;

    public PreoccupiedStock toModel() {
        return PreoccupiedStock.builder()
                .id(this.id)
                .orderId(this.orderId)
                .productId(this.productId)
                .quantity(this.quantity)
                .createdAt(this.createAt)
                .build();
    }

    public static PreoccupiedStockEntity from(PreoccupiedStock preoccupiedStock) {
        return PreoccupiedStockEntity.builder()
                .id(preoccupiedStock.getId())
                .orderId(preoccupiedStock.getOrderId())
                .productId(preoccupiedStock.getProductId())
                .quantity(preoccupiedStock.getQuantity())
                .createAt(preoccupiedStock.getCreatedAt())
                .build();
    }
}
