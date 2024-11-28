package com.example.payment.application.feign.error.decoder;

import com.example.payment.domain.exception.PaymentServiceException;
import com.example.payment.common.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static com.example.payment.domain.exception.ErrorCode.INTERNAL_SERVER_ERROR;

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
            return new RuntimeException(errorResponse.message());
        } catch (IOException e) {
            return new PaymentServiceException(INTERNAL_SERVER_ERROR, e);
        }
    }
}
