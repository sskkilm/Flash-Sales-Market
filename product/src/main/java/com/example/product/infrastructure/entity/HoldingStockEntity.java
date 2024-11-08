package com.example.product.infrastructure.entity;

import com.example.product.domain.HoldingStock;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity(name = "HoldingStock")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class HoldingStockEntity {

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

    public HoldingStock toModel() {
        return HoldingStock.builder()
                .id(this.id)
                .orderId(this.orderId)
                .productId(this.productId)
                .quantity(this.quantity)
                .createdAt(this.createAt)
                .build();
    }

    public static HoldingStockEntity from(HoldingStock holdingStock) {
        return HoldingStockEntity.builder()
                .id(holdingStock.getId())
                .orderId(holdingStock.getOrderId())
                .productId(holdingStock.getProductId())
                .quantity(holdingStock.getQuantity())
                .createAt(holdingStock.getCreatedAt())
                .build();
    }
}
