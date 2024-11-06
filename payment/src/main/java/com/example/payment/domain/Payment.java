package com.example.payment.domain;

import com.example.payment.exception.PaymentServiceException;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.example.payment.domain.PaymentStatus.CONFIRMED;
import static com.example.payment.domain.PaymentStatus.READY;
import static com.example.payment.exception.error.ErrorCode.*;

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
                .status(READY)
                .build();
    }

    public void validate(Long orderId, BigDecimal amount) {
        validateAlreadyConfirmed();
        validateOrderId(orderId);
        validateAmount(amount);
    }

    public void confirmed(String paymentKey) {
        this.paymentKey = paymentKey;
        this.status = CONFIRMED;
    }

    private void validateAlreadyConfirmed() {
        if (this.status == CONFIRMED) {
            throw new PaymentServiceException(PAYMENT_ALREADY_CONFIRMED);
        }
    }

    private void validateOrderId(Long orderId) {
        if (Objects.equals(this.orderId, orderId)) {
            return;
        }

        throw new PaymentServiceException(ORDER_ID_DOES_NOT_MATCH);
    }

    private void validateAmount(BigDecimal amount) {
        if (this.amount.compareTo(amount) == 0) {
            return;
        }

        throw new PaymentServiceException(ORDER_AMOUNT_DOES_NOT_MATCH);
    }

}
