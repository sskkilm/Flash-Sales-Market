package com.example.order.presentation;

import com.example.order.application.OrderService;
import com.example.order.common.dto.OrderInfo;
import com.example.order.common.dto.request.CartItemOrderRequest;
import com.example.order.common.dto.request.OrderCreateRequest;
import com.example.order.common.dto.request.OrderValidationRequest;
import com.example.order.common.dto.response.CartOrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders/internal")
public class OrderInternalController {

    private final OrderService orderService;

    @PostMapping("/cart-items")
    public CartOrderResponse cartOrder(
            @RequestHeader("X-Member-Id") Long memberId,
            @RequestBody List<CartItemOrderRequest> requests
    ) {
        List<OrderInfo> orderInfos = requests
                .stream()
                .map(request -> new OrderInfo(request.productId(), request.quantity()))
                .toList();
        return CartOrderResponse.from(
                memberId,
                orderService.create(memberId, new OrderCreateRequest(orderInfos))
        );
    }

    @PostMapping("/validate")
    public boolean validateOrderInfo(
            @RequestHeader("X-User-Id") Long memberId,
            @RequestBody @Valid OrderValidationRequest request
    ) {
        return orderService.validateOrderInfo(memberId, request);
    }

}
