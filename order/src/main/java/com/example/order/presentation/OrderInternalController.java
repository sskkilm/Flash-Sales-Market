package com.example.order.presentation;

import com.example.order.application.OrderService;
import com.example.order.common.dto.OrderDto;
import com.example.order.common.dto.request.OrderCreateRequest;
import com.example.order.common.dto.response.OrderCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders/internal")
public class OrderInternalController {

    private static final String X_MEMBER_ID = "X-Member-Id";
    private final OrderService orderService;

    @PostMapping("/cart-items")
    public OrderCreateResponse create(
            @RequestHeader(X_MEMBER_ID) Long memberId,
            @RequestBody @Valid OrderCreateRequest orderCreateRequest
    ) {
        return orderService.create(memberId, orderCreateRequest);
    }

    @GetMapping("/{orderId}")
    public OrderDto getOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }
}
