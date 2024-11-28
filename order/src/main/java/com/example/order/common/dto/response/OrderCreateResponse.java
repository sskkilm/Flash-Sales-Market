package com.example.order.common.dto.response;

import java.util.List;

public record OrderCreateResponse(
        Long orderId,
        List<Long> orderProductIds
) {
}
