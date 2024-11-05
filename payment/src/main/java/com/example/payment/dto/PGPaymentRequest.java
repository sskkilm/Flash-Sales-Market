package com.example.payment.dto;

public record PGPaymentRequest(
        OrderInfo orderInfo,
        PaymentInfo paymentInfo
) {
}
