package com.example.order.common.exception;

public record ErrorResponse(
        String code,
        String message
) {

}
