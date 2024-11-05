package com.example.payment.exception.error;

public record ErrorResponse(
        ErrorCode code,
        String message
) {
}
