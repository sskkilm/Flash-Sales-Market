package com.example.payment.application.port;

import com.example.payment.domain.Payment;

import java.util.Optional;

public interface PaymentRepository {

    Payment save(Payment payment);

    Payment findByOrderId(Long orderId);

    Optional<Payment> findOptionalPaymentByOrderId(Long orderId);
}
