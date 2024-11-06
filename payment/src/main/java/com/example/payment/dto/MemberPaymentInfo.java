package com.example.payment.dto;

public record MemberPaymentInfo(
        boolean canInitiated,
        boolean canConfirmed
) {
}
