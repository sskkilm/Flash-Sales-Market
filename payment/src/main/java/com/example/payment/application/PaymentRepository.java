package com.example.payment.application;

import com.example.payment.domain.Payment;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface PaymentRepository {

    Payment save(Payment payment);

    Payment findByOrderId(Long orderId);

    Optional<Payment> findOptionalPaymentByOrderId(Long orderId);
}
