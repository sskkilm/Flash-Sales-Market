package com.example.order.application.feign;

import com.example.order.application.feign.error.decoder.FeignErrorDecoder;
import com.example.order.common.dto.ProductDto;
import com.example.order.common.dto.request.StockIncreaseRequest;
import com.example.order.common.dto.request.StockDecreaseRequest;
import com.example.order.common.dto.request.StockPreoccupationRequest;
import com.example.order.common.dto.response.StockPreoccupationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "api-gateway",
        path = "/products/internal",
        contextId = "productClient",
        configuration = FeignErrorDecoder.class)
public interface ProductFeignClient {

    @GetMapping("/{productId}")
    ProductDto getProductInfo(@PathVariable Long productId);

    @PostMapping("/preoccupation")
    StockPreoccupationResponse preoccupyStock(
            @RequestBody StockPreoccupationRequest stockPreoccupationRequest
    );

    @PostMapping("/increase/stock")
    void increaseStock(@RequestBody List<StockIncreaseRequest> stockIncreaseRequests);

    @PostMapping("/decrease/stock")
    void decreaseStock(@RequestBody List<StockDecreaseRequest> stockDecreaseRequests);
}
