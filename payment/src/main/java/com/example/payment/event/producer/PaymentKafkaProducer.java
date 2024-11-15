package com.example.payment.event.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentKafkaProducer {

    private static final String TOPIC = "payment";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}
