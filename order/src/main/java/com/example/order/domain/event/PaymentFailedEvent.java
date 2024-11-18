package com.example.order.domain.event;

public record PaymentFailedEvent(
        Long orderId
) {
}
