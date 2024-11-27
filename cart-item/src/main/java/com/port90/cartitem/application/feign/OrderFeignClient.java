package com.port90.cartitem.application.feign;

import com.port90.cartitem.common.dto.request.CartItemOrderRequest;
import com.port90.cartitem.common.dto.response.CartOrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "api-gateway", path = "/orders/internal", contextId = "orderClient")
public interface OrderFeignClient {

    @PostMapping("/cart-items")
    CartOrderResponse cartOrder(
            @RequestHeader("X-Member-Id") Long memberId,
            @RequestBody List<CartItemOrderRequest> cartItemOrderRequests
    );
}
