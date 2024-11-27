package com.example.order.common.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record OrderCreateResponse(
        Long orderId,
        List<Long> orderProductIds,
        BigDecimal totalAmount
) {
}
