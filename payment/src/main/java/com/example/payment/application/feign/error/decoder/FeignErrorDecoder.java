package com.example.payment.application.feign.error.decoder;

import com.example.payment.exception.PaymentServiceException;
import com.example.payment.exception.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static com.example.payment.exception.error.ErrorCode.INTERNAL_SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
public class FeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        try {
            ErrorResponse errorResponse = objectMapper.readValue(
                    response.body().asInputStream(), ErrorResponse.class
            );
            log.info("Error Http Status: {}", response.status());
            log.info("Error Code: {}", errorResponse.code());
            log.info("Error Code Message: {}", errorResponse.message());
            return new PaymentServiceException(
                    status, errorResponse.code(), errorResponse.message()
            );
        } catch (IOException e) {
            log.info("Error Message: {}", e.getMessage());
            return new PaymentServiceException(INTERNAL_SERVER_ERROR);
        }
    }
}
