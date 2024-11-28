package com.example.payment.domain;

import com.example.payment.domain.exception.PaymentServiceException;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.example.payment.domain.PaymentStatus.*;
import static com.example.payment.domain.exception.ErrorCode.INVALID_ORDER_AMOUNT;
import static com.example.payment.domain.exception.ErrorCode.PAYMENT_ALREADY_CONFIRMED;

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

    public static Payment create(Long orderId, BigDecimal amount) {
        return Payment.builder()
                .orderId(orderId)
                .amount(amount)
                .status(PENDING)
                .build();
    }

    public void failed() {
        this.status = FAILED;
    }

    public void validate(BigDecimal amount) {
        if (this.status == CONFIRMED) {
            throw new PaymentServiceException(PAYMENT_ALREADY_CONFIRMED);
        }
        if (this.amount.compareTo(amount) != 0) {
            throw new PaymentServiceException(INVALID_ORDER_AMOUNT);
        }
    }

    public boolean isPending() {
        return this.status == PENDING;
    }

    public void updatePaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
    }

    public void confirmed() {
        this.status = CONFIRMED;
    }
}
