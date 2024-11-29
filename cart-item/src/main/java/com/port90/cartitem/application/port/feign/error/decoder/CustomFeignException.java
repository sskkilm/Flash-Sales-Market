package com.port90.cartitem.application.port.feign.error.decoder;

import com.port90.cartitem.common.exception.ErrorResponse;
import lombok.Getter;

@Getter
public class CustomFeignException extends RuntimeException {

    private final ErrorResponse errorResponse;

    public CustomFeignException(ErrorResponse errorResponse) {
        super(errorResponse.message());
        this.errorResponse = errorResponse;
    }
}
