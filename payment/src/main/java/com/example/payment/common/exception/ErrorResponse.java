package com.example.payment.common.exception;

public record ErrorResponse(
        String code,
        String message
) {
}
