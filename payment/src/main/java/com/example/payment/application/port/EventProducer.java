package com.example.payment.application.port;

import com.example.payment.domain.event.PaymentConfirmedEvent;
import com.example.payment.domain.event.PaymentFailedEvent;

public interface EventProducer {

    void publish(PaymentConfirmedEvent event);

    void publish(PaymentFailedEvent event);

}
