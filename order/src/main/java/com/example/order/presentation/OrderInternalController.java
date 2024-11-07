package com.example.order.presentation;

import com.example.order.application.OrderService;
import com.example.order.dto.OrderValidationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders/internal")
public class OrderInternalController {

    private final OrderService orderService;

    @PostMapping("/{memberId}/validate")
    public boolean validateOrderInfo(
            @PathVariable Long memberId,
            @RequestBody @Valid OrderValidationRequest request
    ) {
        return orderService.validateOrderInfo(memberId, request);
    }

    @PostMapping("/{orderId}/payment/completed")
    public void paymentCompleted(@PathVariable Long orderId) {
        orderService.paymentCompleted(orderId);
    }
}
