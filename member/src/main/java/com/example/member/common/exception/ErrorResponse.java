package com.example.member.common.exception;

public record ErrorResponse(
        String code,
        String message
) {
}
