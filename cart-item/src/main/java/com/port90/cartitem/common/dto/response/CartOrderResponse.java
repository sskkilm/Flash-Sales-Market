package com.port90.cartitem.common.dto.response;

import java.util.List;

public record CartOrderResponse(
        Long orderId,
        Long memberId,
        List<Long> orderProductIds
) {
}
