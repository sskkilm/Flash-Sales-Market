package com.example.payment.dto;

import jakarta.validation.constraints.NotNull;

public record PaymentInitRequest(
        @NotNull OrderInfo orderInfo,
        @NotNull PaymentInfo paymentInfo
) {
}
