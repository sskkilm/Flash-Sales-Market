package com.example.order.presentation;

import com.example.order.application.OrderService;
import com.example.order.common.dto.OrderDto;
import com.example.order.common.dto.request.OrderCreateRequest;
import com.example.order.common.dto.response.OrderCancelResponse;
import com.example.order.common.dto.response.OrderCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private static final String X_MEMBER_ID = "X-Member-Id";
    private final OrderService orderService;

    @PostMapping
    public OrderCreateResponse create(
            @RequestHeader(X_MEMBER_ID) Long memberId,
            @RequestBody @Valid OrderCreateRequest orderCreateRequest
    ) {
        return orderService.create(memberId, orderCreateRequest);
    }

    @PostMapping("/{orderId}/cancel")
    public OrderCancelResponse cancel(
            @RequestHeader(X_MEMBER_ID) Long memberId,
            @PathVariable Long orderId
    ) {
        return orderService.cancel(memberId, orderId);
    }

    @GetMapping
    public List<OrderDto> getOrderList(
            @RequestHeader(X_MEMBER_ID) Long memberId
    ) {
        return orderService.getOrderList(memberId);
    }

}
