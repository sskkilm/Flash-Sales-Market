package com.port90.cartitem.application.port.feign;

import com.port90.cartitem.application.port.feign.error.decoder.FeignErrorDecoder;
import com.port90.cartitem.common.dto.request.OrderCreateRequest;
import com.port90.cartitem.common.dto.response.OrderCreateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "api-gateway",
        path = "/orders/internal",
        contextId = "orderClient",
        configuration = FeignErrorDecoder.class
)
public interface OrderClient {

    @PostMapping("/cart-items")
    OrderCreateResponse create(
            @RequestHeader("X-Member-Id") Long memberId,
            @RequestBody OrderCreateRequest orderCreateRequest
    );
}
