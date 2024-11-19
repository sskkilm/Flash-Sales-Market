package com.example.product.domain.event;

public record PaymentFailedEvent(
        Long orderId
) {
}
