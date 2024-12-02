package com.example.payment.presentation;

import com.example.payment.application.PaymentService;
import com.example.payment.common.dto.PaymentDto;
import com.example.payment.common.dto.request.PaymentConfirmRequest;
import com.example.payment.common.dto.request.PaymentInitRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private static final String X_MEMBER_ID = "X-Member-Id";
    private final PaymentService paymentService;

    @PostMapping("/init")
    public ResponseEntity<?> init(
            @RequestHeader(X_MEMBER_ID) Long memberId,
            @RequestBody @Valid PaymentInitRequest paymentInitRequest
    ) {
        paymentService.init(memberId, paymentInitRequest);
        return ResponseEntity
                .ok()
                .body("Payment initiated");
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(
            @RequestBody @Valid PaymentConfirmRequest request
    ) {
        paymentService.confirm(request);
        return ResponseEntity
                .ok()
                .body("Payment confirmed");
    }

    @GetMapping
    public List<PaymentDto> getPaymentList(
            @RequestHeader(X_MEMBER_ID) Long memberId
    ) {
        return paymentService.getPaymentList(memberId);
    }
}
