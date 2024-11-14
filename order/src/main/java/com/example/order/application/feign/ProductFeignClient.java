package com.example.order.application.feign;

import com.example.order.dto.OrderCompletedProductDto;
import com.example.order.dto.StockHoldRequest;
import com.example.order.dto.StockHoldResponse;
import com.example.order.dto.ProductRestockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "api-gateway", path = "/products/internal", contextId = "productClient")
public interface ProductFeignClient {

    @PostMapping("/hold-stock")
    StockHoldResponse holdStock(
            @RequestBody StockHoldRequest stockHoldRequest
    );

    @PostMapping("/restock")
    void restock(
            @RequestBody ProductRestockRequest productRestockRequest
    );

    @PostMapping("/decrease/stock")
    void decreaseStock(@RequestBody List<OrderCompletedProductDto> list);

}
