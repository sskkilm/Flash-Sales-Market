package com.example.product.exception.error;

public record ErrorResponse(
        ErrorCode code,
        String message
) {
    public ErrorResponse(ErrorCode code) {
        this(code, code.getMessage());
    }
}
