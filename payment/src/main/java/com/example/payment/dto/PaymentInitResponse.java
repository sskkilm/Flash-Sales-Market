package com.example.payment.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

public record PaymentInitResponse(
        String paymentKey,
        Long orderId,
        BigDecimal amount
) {
    public static PaymentInitResponse from(PGInitResponse pgInitResponse) {
        return new PaymentInitResponse(
                pgInitResponse.paymentKey(),
                pgInitResponse.orderId(),
                pgInitResponse.amount()
        );
    }

    public ResponseEntity<?> redirectToConfirm() {
        URI redirectUri = UriComponentsBuilder.fromUriString("http://localhost:8080")
                .path("/payments/confirm")
                .queryParam("paymentKey", this.paymentKey)
                .queryParam("orderId", this.orderId)
                .queryParam("amount", this.amount)
                .build()
                .toUri();

        return ResponseEntity
                .status(HttpStatus.PERMANENT_REDIRECT)
                .location(redirectUri)
                .build();
    }
}
