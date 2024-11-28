package com.example.order.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.example.order.domain.OrderStatus.*;

@Getter
@Builder
public class Order {

    private Long id;
    private Long memberId;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Order create(Long memberId) {
        return Order.builder()
                .memberId(memberId)
                .status(PENDING_PAYMENT)
                .build();
    }

    public void paymentFailed() {
        this.status = PAYMENT_FAILED;
    }

    public void paymentConfirmed() {
        this.status = PAYMENT_CONFIRMED;
    }
}
