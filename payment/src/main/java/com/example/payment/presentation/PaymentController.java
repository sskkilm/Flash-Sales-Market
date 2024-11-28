package com.example.payment.presentation;

import com.example.payment.application.PaymentService;
import com.example.payment.common.dto.request.PaymentInitRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private static final String X_MEMBER_ID = "X-Member-Id";
    private final PaymentService paymentService;

    @PostMapping("/init")
    public void init(
            @RequestHeader(X_MEMBER_ID) Long memberId,
            @RequestBody @Valid PaymentInitRequest paymentInitRequest
    ) {
        paymentService.init(memberId, paymentInitRequest);
    }

    @PostMapping("/confirm")
    public void confirm(
            @RequestParam String paymentKey,
            @RequestParam Long orderId,
            @RequestParam BigDecimal amount
    ) {
        paymentService.confirm(paymentKey, orderId, amount);
    }
}
