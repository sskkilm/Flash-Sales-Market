package com.example.payment.exception.error;

public record ErrorResponse(
        String code,
        String message
) {
}
