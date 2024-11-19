package com.example.product.infrastructure.consumer;

import com.example.product.application.ProductService;
import com.example.product.domain.event.PaymentConfirmedEvent;
import com.example.product.domain.event.PaymentFailedEvent;
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

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "payment-confirmed-events", groupId = "product-payment-confirmed-event-consumer", containerFactory = "paymentConfirmedEventKafkaListenerContainerFactory")
    public void consumePaymentConfirmedEvent(String message) {
        try {
            PaymentConfirmedEvent paymentConfirmedEvent = objectMapper.readValue(message, PaymentConfirmedEvent.class);
            log.info("Consume Payment Confirmed Event: {}", paymentConfirmedEvent);
            productService.applyPreoccupiedStock(paymentConfirmedEvent.orderId(), paymentConfirmedEvent.orderProductIds());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("역직렬화 실패", e);
        }
    }

    @KafkaListener(topics = "payment-failed-events", groupId = "product-payment-failed-event-consumer", containerFactory = "paymentFailedEventKafkaListenerContainerFactory")
    public void consumePaymentFailedEvent(String message) {
        try {
            PaymentFailedEvent paymentFailedEvent = objectMapper.readValue(message, PaymentFailedEvent.class);
            log.info("Consume Payment Failed Event: {}", paymentFailedEvent);
            productService.releasePreoccupiedStock(paymentFailedEvent.orderId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("역직렬화 실패", e);
        }
    }

}
