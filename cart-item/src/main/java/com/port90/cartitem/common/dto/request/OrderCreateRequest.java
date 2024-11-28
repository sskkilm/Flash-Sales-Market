package com.port90.cartitem.common.dto.request;

import com.port90.cartitem.common.dto.OrderInfo;

import java.util.List;

public record OrderCreateRequest(
        List<OrderInfo> orderInfos
) {
}
