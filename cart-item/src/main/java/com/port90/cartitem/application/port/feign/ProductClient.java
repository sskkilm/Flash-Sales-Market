package com.port90.cartitem.application.port.feign;

import com.port90.cartitem.application.port.feign.error.decoder.FeignErrorDecoder;
import com.port90.cartitem.common.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "api-gateway",
        path = "/products/internal",
        contextId = "productClient",
        configuration = FeignErrorDecoder.class
)
public interface ProductClient {

    @GetMapping("/{productId}")
    ProductDto getProductInfo(@PathVariable Long productId);
}
