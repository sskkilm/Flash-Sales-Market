package com.example.payment.application.port.feign.error.decoder;

import com.example.payment.common.exception.ErrorResponse;
import lombok.Getter;

@Getter
public class CustomFeignException extends RuntimeException {

    private final ErrorResponse errorResponse;

    public CustomFeignException(ErrorResponse errorResponse) {
        super(errorResponse.message());
        this.errorResponse = errorResponse;
    }
}
