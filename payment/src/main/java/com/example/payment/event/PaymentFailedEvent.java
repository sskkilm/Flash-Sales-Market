package com.example.payment.event;

public record PaymentFailedEvent(
        Long orderId
) {
}
