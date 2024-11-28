package com.example.order.common.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderDto(
        Long orderId,
        Long memberId,
        String status,
        BigDecimal amount,
        List<OrderProductDto> orderProducts
) {
}
