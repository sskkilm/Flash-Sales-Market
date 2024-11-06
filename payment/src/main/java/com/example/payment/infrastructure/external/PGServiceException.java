package com.example.payment.infrastructure.external;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PGServiceException extends RuntimeException {
    private final String message;
}
