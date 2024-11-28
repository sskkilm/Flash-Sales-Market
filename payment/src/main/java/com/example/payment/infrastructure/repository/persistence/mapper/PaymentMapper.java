package com.example.payment.infrastructure.repository.persistence.mapper;

import com.example.payment.domain.Payment;
import com.example.payment.domain.PaymentStatus;
import com.example.payment.infrastructure.repository.persistence.entity.PaymentEntity;

public class PaymentMapper {

    public static PaymentEntity toEntity(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .paymentKey(payment.getPaymentKey())
                .status(payment.getStatus().name())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }

    public static Payment toModel(PaymentEntity paymentEntity) {
        return Payment.builder()
                .id(paymentEntity.getId())
                .orderId(paymentEntity.getOrderId())
                .amount(paymentEntity.getAmount())
                .paymentKey(paymentEntity.getPaymentKey())
                .status(PaymentStatus.valueOf(paymentEntity.getStatus()))
                .createdAt(paymentEntity.getCreatedAt())
                .updatedAt(paymentEntity.getUpdatedAt())
                .build();
    }
}
