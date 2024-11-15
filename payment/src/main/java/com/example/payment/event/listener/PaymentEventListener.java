package com.example.payment.event.listener;

import com.example.payment.event.PaymentConfirmedEvent;
import com.example.payment.event.PaymentFailedEvent;
import com.example.payment.event.producer.PaymentKafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final PaymentKafkaProducer producer;
    private final ObjectMapper objectMapper;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentConfirm(PaymentConfirmedEvent event) {
        try {
            producer.send(objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            // TODO: MessageProcessingException 으로 변경
            throw new RuntimeException("Message Processing Exception", e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentFailed(PaymentFailedEvent event) {
        try {
            producer.send(objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            // TODO: MessageProcessingException 으로 변경
            throw new RuntimeException("Message Processing Exception", e);
        }
    }
}
