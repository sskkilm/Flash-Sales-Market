package com.example.order.application.feign;

import com.example.order.dto.ProductOrderRequest;
import com.example.order.dto.ProductOrderResponse;
import com.example.order.dto.ProductRestockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "api-gateway", path = "/products", contextId = "productClient")
public interface ProductFeignClient {

    @PostMapping("/internal/order")
    ProductOrderResponse getProductOrderInfo(
            @RequestBody ProductOrderRequest productOrderRequest
    );

    @PostMapping("/internal/restock")
    void restock(
            @RequestBody ProductRestockRequest productRestockRequest
    );

}
