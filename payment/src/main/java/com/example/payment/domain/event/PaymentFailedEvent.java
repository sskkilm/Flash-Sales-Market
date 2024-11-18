package com.example.payment.domain.event;

public record PaymentFailedEvent(
        Long orderId
) {
}
