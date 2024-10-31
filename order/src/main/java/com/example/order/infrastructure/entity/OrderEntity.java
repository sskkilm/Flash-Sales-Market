package com.example.order.infrastructure.entity;

import com.example.order.domain.Order;
import com.example.order.domain.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity(name = "Order")
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static OrderEntity from(Order order) {
        return OrderEntity.builder()
                .id(order.getId())
                .memberId(order.getMemberId())
                .status(order.getStatus().toString())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    public Order toModel() {
        return Order.builder()
                .id(this.id)
                .memberId(this.memberId)
                .status(OrderStatus.valueOf(this.status))
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
