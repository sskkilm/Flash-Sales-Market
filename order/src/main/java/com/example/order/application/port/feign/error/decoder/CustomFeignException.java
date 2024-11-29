package com.example.order.application.port.feign.error.decoder;

import com.example.order.common.exception.ErrorResponse;
import lombok.Getter;

@Getter
public class CustomFeignException extends RuntimeException {

    private final ErrorResponse errorResponse;

    public CustomFeignException(ErrorResponse errorResponse) {
        super(errorResponse.message());
        this.errorResponse = errorResponse;
    }
}
