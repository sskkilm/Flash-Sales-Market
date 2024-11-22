package com.example.order.dto;

import com.example.order.domain.Order;

import java.util.List;

public record StockPreoccupationRequest(
        Long orderId,
        List<StockPreoccupationInfo> stockPreoccupationInfos
) {
    public static StockPreoccupationRequest from(Order order, List<OrderInfo> orderInfos) {
        return new StockPreoccupationRequest(
                order.getId(),
                orderInfos.stream().map(
                        orderInfo -> new StockPreoccupationInfo(
                                orderInfo.productId(), orderInfo.quantity()
                        )
                ).toList());
    }
}
