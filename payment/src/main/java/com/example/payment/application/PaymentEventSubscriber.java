package com.example.payment.application;

import com.example.payment.application.port.EventProducer;
import com.example.payment.domain.event.PaymentConfirmedEvent;
import com.example.payment.domain.event.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventSubscriber {

    private final EventProducer eventProducer;

    @EventListener
    public void handlePaymentConfirmedEvent(PaymentConfirmedEvent event) {
        eventProducer.publish(event);
    }

    @EventListener
    public void handlePaymentFailedEvent(PaymentFailedEvent event) {
        eventProducer.publish(event);
    }
}
