package com.example.payment.application;

import com.example.payment.domain.Payment;

public interface PaymentRepository {

    Payment save(Payment payment);

}
