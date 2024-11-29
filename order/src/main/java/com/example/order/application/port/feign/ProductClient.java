package com.example.order.application.port.feign;

import com.example.order.application.port.feign.error.decoder.FeignErrorDecoder;
import com.example.order.common.dto.ProductDto;
import com.example.order.common.dto.request.StockDecreaseRequest;
import com.example.order.common.dto.request.StockIncreaseRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "api-gateway",
        path = "/products/internal",
        contextId = "productClient",
        configuration = FeignErrorDecoder.class
)
public interface ProductClient {

    @GetMapping("/{productId}")
    ProductDto getProductInfo(@PathVariable Long productId);

    @PostMapping("/decrease/stock")
    void decreaseStock(@RequestBody List<StockDecreaseRequest> stockDecreaseRequests);

    @PostMapping("/increase/stock")
    void increaseStock(@RequestBody List<StockIncreaseRequest> stockIncreaseRequests);
}
