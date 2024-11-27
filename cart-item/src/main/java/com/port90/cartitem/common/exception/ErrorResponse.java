package com.port90.cartitem.common.exception;

public record ErrorResponse(
        String code,
        String message
) {
}
