package com.example.payment.common.dto.request;

import java.math.BigDecimal;
import java.util.List;

public record OrderValidationRequest(
        Long orderId,
        List<Long> orderProductIds,
        BigDecimal amount
) {
}
