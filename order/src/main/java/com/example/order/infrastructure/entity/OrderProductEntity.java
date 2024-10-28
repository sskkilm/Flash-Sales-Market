package com.example.order.infrastructure.entity;

import com.example.order.domain.Money;
import com.example.order.domain.OrderProduct;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "OrderProduct")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal orderAmount;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static OrderProductEntity from(OrderProduct orderProduct) {
        return OrderProductEntity.builder()
                .id(orderProduct.getId())
                .order(OrderEntity.from(orderProduct.getOrder()))
                .productId(orderProduct.getProductId())
                .name(orderProduct.getName())
                .quantity(orderProduct.getQuantity())
                .orderAmount(orderProduct.getOrderAmount().amount())
                .createdAt(orderProduct.getCreatedAt())
                .build();
    }

    public OrderProduct toModel() {
        return OrderProduct.builder()
                .id(this.id)
                .order(this.order.toModel())
                .productId(this.productId)
                .name(this.name)
                .quantity(this.quantity)
                .orderAmount(Money.of(this.orderAmount.toString()))
                .createdAt(this.createdAt)
                .build();
    }
}
