package com.example.order.dto;

import java.util.List;

public record ProductPurchaseResponse(
        List<PurchasedProductInfo> purchasedProductInfos
) {
}
