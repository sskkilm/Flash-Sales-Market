package com.example.product.dto;

import java.util.List;

public record ProductPurchaseResponse(
        List<PurchasedProductInfo> purchasedProductInfos
) {
}
