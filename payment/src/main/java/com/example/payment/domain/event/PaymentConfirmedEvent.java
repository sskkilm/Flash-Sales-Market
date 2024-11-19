package com.example.payment.domain.event;

import java.util.List;

public record PaymentConfirmedEvent(
        Long orderId,
        List<Long> orderProductIds
) {
}
