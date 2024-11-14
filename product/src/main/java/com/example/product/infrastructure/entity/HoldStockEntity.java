package com.example.product.infrastructure.entity;

import com.example.product.domain.HoldStock;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity(name = "HoldStock")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class HoldStockEntity {

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

    public HoldStock toModel() {
        return HoldStock.builder()
                .id(this.id)
                .orderId(this.orderId)
                .productId(this.productId)
                .quantity(this.quantity)
                .createdAt(this.createAt)
                .build();
    }

    public static HoldStockEntity from(HoldStock holdStock) {
        return HoldStockEntity.builder()
                .id(holdStock.getId())
                .orderId(holdStock.getOrderId())
                .productId(holdStock.getProductId())
                .quantity(holdStock.getQuantity())
                .createAt(holdStock.getCreatedAt())
                .build();
    }
}
