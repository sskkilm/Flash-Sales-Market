package com.example.payment.application.port.feign.error.decoder;

import com.example.payment.common.exception.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        String responseBody = extractResponseBody(response);
        ErrorResponse errorResponse = getErrorResponse(responseBody);
        switch (response.status()) {
            case 400:
                return new CustomFeignException(errorResponse);
            case 500:
                return new RuntimeException(errorResponse.message());
            default:
                return new Exception(errorResponse.message());
        }
    }

    private ErrorResponse getErrorResponse(String responseBody) {
        try {
            return objectMapper.readValue(responseBody, ErrorResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse error response", e);
        }
    }

    private String extractResponseBody(Response response) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse response body", e);
        }
    }
}
