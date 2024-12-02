package com.example.payment.common.dto;

import com.example.payment.domain.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentDto(
        Long paymentId,
        Long orderId,
        BigDecimal amount,
        String paymentKey,
        String status,
        LocalDateTime createdAt
) {
    public static PaymentDto from(Payment payment) {
        return new PaymentDto(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getPaymentKey(),
                payment.getStatus().name(),
                payment.getCreatedAt()
        );
    }
}
