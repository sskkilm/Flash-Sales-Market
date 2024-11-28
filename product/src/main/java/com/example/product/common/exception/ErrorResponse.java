package com.example.product.common.exception;

import com.example.product.domain.exception.ErrorCode;

public record ErrorResponse(
        ErrorCode code,
        String message
) {
    public ErrorResponse(ErrorCode code) {
        this(code, code.getMessage());
    }
}
