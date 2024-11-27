package com.example.order.common.dto.request;

import com.example.order.common.dto.StockPreoccupationInfo;

import java.util.List;

public record StockPreoccupationRequest(
        Long orderId,
        List<StockPreoccupationInfo> stockPreoccupationInfos
) {
}
