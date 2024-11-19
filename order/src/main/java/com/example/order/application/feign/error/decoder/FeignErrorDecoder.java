package com.example.order.application.feign.error.decoder;

import com.example.order.exception.OrderServiceException;
import com.example.order.exception.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static com.example.order.exception.error.ErrorCode.INTERNAL_SERVER_ERROR;

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
            return new OrderServiceException(
                    status, errorResponse.code(), errorResponse.message()
            );
        } catch (IOException e) {
            return new OrderServiceException(INTERNAL_SERVER_ERROR);
        }
    }
}
