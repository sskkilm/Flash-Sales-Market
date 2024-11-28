package com.example.payment.application.port;

import com.example.payment.domain.Payment;

public interface PaymentRepository {

    Payment save(Payment payment);

    Payment findByOrderId(Long orderId);

    void rollBack(Payment payment);
}
