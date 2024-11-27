package com.example.order.application.feign;

import com.example.order.application.feign.error.decoder.FeignErrorDecoder;
import com.example.order.common.dto.OrderCompletedProductDto;
import com.example.order.common.dto.request.ProductRestockRequest;
import com.example.order.common.dto.request.StockPreoccupationRequest;
import com.example.order.common.dto.response.StockPreoccupationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "api-gateway",
        path = "/products/internal",
        contextId = "productClient",
        configuration = FeignErrorDecoder.class)
public interface ProductFeignClient {

    @PostMapping("/preoccupation")
    StockPreoccupationResponse preoccupyStock(
            @RequestBody StockPreoccupationRequest stockPreoccupationRequest
    );

    @PostMapping("/restock")
    void restock(
            @RequestBody ProductRestockRequest productRestockRequest
    );

    @PostMapping("/decrease/stock")
    void decreaseStock(@RequestBody List<OrderCompletedProductDto> list);

}
