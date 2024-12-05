package com.example.order.domain.event;

public record PaymentConfirmedEvent(
        Long orderId
) {
}
