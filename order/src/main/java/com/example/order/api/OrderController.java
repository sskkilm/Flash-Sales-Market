package com.example.order.api;

import com.example.order.application.OrderService;
import com.example.order.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{memberId}")
    public OrderCreateResponse create(
            @PathVariable Long memberId,
            @RequestBody @Valid OrderCreateRequest orderCreateRequest
    ) {
        return orderService.create(memberId, orderCreateRequest);
    }

    @PostMapping("/{memberId}/{orderId}/cancel")
    public OrderCancelResponse cancel(
            @PathVariable Long memberId,
            @PathVariable Long orderId
    ) {
        return orderService.cancel(memberId, orderId);
    }

    @PostMapping("/{memberId}/{orderId}/return")
    public OrderReturnResponse returns(
            @PathVariable Long memberId,
            @PathVariable Long orderId
    ) {
        return orderService.returns(memberId, orderId);
    }

    @GetMapping("/{memberId}")
    public List<OrderHistory> getOrderHistories(
            @PathVariable Long memberId
    ) {
        return orderService.getOrderHistories(memberId);
    }

}
