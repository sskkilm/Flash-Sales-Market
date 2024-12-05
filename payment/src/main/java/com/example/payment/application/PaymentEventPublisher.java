package com.example.payment.application;

import com.example.payment.domain.Payment;
import com.example.payment.domain.event.PaymentConfirmedEvent;
import com.example.payment.domain.event.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishPaymentConfirmedEvent(Payment payment) {
        PaymentConfirmedEvent event = new PaymentConfirmedEvent(payment.getOrderId());
        eventPublisher.publishEvent(event);
    }

    public void publishPaymentFailedEvent(Long orderId) {
        PaymentFailedEvent event = new PaymentFailedEvent(orderId);
        eventPublisher.publishEvent(event);
    }
}
