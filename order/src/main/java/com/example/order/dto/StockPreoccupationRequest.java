package com.example.order.dto;

import java.util.List;

public record StockPreoccupationRequest(
        Long orderId,
        List<StockPreoccupationInfo> stockPreoccupationInfos
) {
}
