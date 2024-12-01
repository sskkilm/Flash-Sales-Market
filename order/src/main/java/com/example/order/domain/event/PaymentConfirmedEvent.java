package com.example.order.domain.event;

import java.util.List;

public record PaymentConfirmedEvent(
        Long orderId,
        List<Long> orderProductIds
) {
}
