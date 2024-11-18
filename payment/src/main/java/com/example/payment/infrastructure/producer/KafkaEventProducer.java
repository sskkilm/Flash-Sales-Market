package com.example.payment.infrastructure.producer;

import com.example.payment.application.port.EventProducer;
import com.example.payment.domain.event.PaymentConfirmedEvent;
import com.example.payment.domain.event.PaymentFailedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventProducer implements EventProducer {

    private static final String PAYMENT_CONFIRMED_TOPIC = "payment-confirmed-events";
    private static final String PAYMENT_FAILED_TOPIC = "payment-failed-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(PaymentConfirmedEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(PAYMENT_CONFIRMED_TOPIC, message);
            log.info("Publish Payment Confirmed Event: {}", event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("직렬화 실패", e);
        }
    }

    @Override
    public void publish(PaymentFailedEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(PAYMENT_FAILED_TOPIC, message);
            log.info("Publish Payment Failed Event: {}", event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("직렬화 실패", e);
        }
    }

}
