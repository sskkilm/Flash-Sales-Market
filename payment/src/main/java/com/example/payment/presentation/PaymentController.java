package com.example.payment.presentation;

import com.example.payment.application.PaymentService;
import com.example.payment.dto.PaymentConfirmResponse;
import com.example.payment.dto.PaymentInitRequest;
import com.example.payment.dto.PaymentInitResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/init/{memberId}")
    public ResponseEntity<?> init(
            @PathVariable Long memberId,
            @RequestBody @Valid PaymentInitRequest paymentInitRequest
    ) {
        PaymentInitResponse response = paymentService.init(memberId, paymentInitRequest);
        return response.redirectToConfirm();
    }

    @PostMapping("/confirm")
    public PaymentConfirmResponse confirm(
            @RequestParam String paymentKey,
            @RequestParam Long orderId,
            @RequestParam BigDecimal amount,
            @RequestBody PaymentInitRequest paymentInitRequest
    ) {
        return paymentService.confirm(
                paymentKey, orderId, amount, paymentInitRequest.memberPaymentInfo()
        );
    }
}
