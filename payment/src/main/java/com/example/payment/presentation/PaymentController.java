package com.example.payment.presentation;

import com.example.payment.application.PaymentService;
import com.example.payment.dto.MemberPaymentInfo;
import com.example.payment.dto.PaymentConfirmResponse;
import com.example.payment.dto.PaymentInitRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/init/{memberId}")
    public void init(
            @PathVariable Long memberId,
            @RequestBody @Valid PaymentInitRequest paymentInitRequest
    ) {
        paymentService.init(memberId, paymentInitRequest);
    }

    @PostMapping("/confirm")
    public PaymentConfirmResponse confirm(
            @RequestParam String paymentKey,
            @RequestParam Long orderId,
            @RequestParam BigDecimal amount,
            @RequestBody MemberPaymentInfo memberPaymentInfo
    ) {
        return paymentService.confirm(
                paymentKey, orderId, amount, memberPaymentInfo
        );
    }
}
