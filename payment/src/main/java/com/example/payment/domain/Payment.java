package com.example.payment.domain;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.example.payment.domain.PaymentStatus.CONFIRMED;

@Getter
@Builder
public class Payment {
    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private String paymentKey;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Payment create(Long orderId, BigDecimal amount, String paymentKey) {
        return Payment.builder()
                .orderId(orderId)
                .amount(amount)
                .paymentKey(paymentKey)
                .status(CONFIRMED)
                .build();
    }
}
