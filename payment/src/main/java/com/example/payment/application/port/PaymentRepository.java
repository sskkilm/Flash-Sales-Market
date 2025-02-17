package com.example.payment.application.port;

import com.example.payment.domain.Payment;

import java.util.List;

public interface PaymentRepository {

    Payment save(Payment payment);

    Payment findByOrderId(Long orderId);

    void deleteById(Long paymentId);

    List<Payment> findByOrderIdInOrderIds(List<Long> orderIds);
}
