package com.port90.cartitem.common.dto.response;

import java.util.List;

public record OrderCreateResponse(
        Long orderId,
        List<Long> orderProductIds
) {
}
