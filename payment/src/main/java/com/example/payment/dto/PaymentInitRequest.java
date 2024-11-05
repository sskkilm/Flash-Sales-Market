package com.example.payment.dto;

public record PaymentInitRequest(
        OrderInfo orderInfo,
        PaymentInfo paymentInfo
) {
}
