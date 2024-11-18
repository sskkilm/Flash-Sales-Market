package com.example.order.infrastructure.consumer;

import com.example.order.application.OrderService;
import com.example.order.domain.event.PaymentConfirmedEvent;
import com.example.order.domain.event.PaymentFailedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventConsumer {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "payment-confirmed-events", groupId = "payment-confirmed-event-consumer", containerFactory = "paymentConfirmedEventKafkaListenerContainerFactory")
    public void consumePaymentConfirmedEvent(String message) {
        try {
            PaymentConfirmedEvent paymentConfirmedEvent = objectMapper.readValue(message, PaymentConfirmedEvent.class);
            log.info("Consume Payment Confirmed Event: {}", paymentConfirmedEvent);
            orderService.updateOrderCompleted(paymentConfirmedEvent.orderId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("역직렬화 실패", e);
        }
    }

    @KafkaListener(topics = "payment-failed-events", groupId = "payment-failed-event-consumer", containerFactory = "paymentFailedEventKafkaListenerContainerFactory")
    public void consumePaymentFailedEvent(String message) {
        try {
            PaymentFailedEvent paymentFailedEvent = objectMapper.readValue(message, PaymentFailedEvent.class);
            log.info("Consume Payment Failed Event: {}", paymentFailedEvent);
            orderService.updateOrderFailed(paymentFailedEvent.orderId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("역직렬화 실패", e);
        }
    }

}
