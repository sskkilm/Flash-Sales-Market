package com.example.payment.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderValidationRequest(
        Long orderId,
        List<Long> orderProductIds,
        BigDecimal amount
) {
}
