package com.example.payment.domain.event;

public record PaymentConfirmedEvent(
        Long orderId
) {
}
