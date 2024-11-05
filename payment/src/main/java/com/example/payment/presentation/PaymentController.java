package com.example.payment.presentation;

import com.example.payment.application.PaymentService;
import com.example.payment.dto.PaymentInitRequest;
import com.example.payment.dto.PaymentInitResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/init/{memberId}")
    public PaymentInitResponse init(
            @PathVariable Long memberId,
            @RequestBody @Valid PaymentInitRequest request) {
        return paymentService.init(memberId, request);
    }
}
