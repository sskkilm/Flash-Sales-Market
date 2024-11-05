package com.example.order.presentation;

import com.example.order.application.OrderService;
import com.example.order.dto.OrderInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders/internal")
public class OrderInternalController {

    private final OrderService orderService;

    @GetMapping("/orders/internal/{memberId}/validate")
    public boolean validateOrderInfo(
            @PathVariable Long memberId,
            @RequestBody @Valid OrderInfo orderInfo
    ) {
        return orderService.validateOrderInfo(memberId, orderInfo);
    }
}
