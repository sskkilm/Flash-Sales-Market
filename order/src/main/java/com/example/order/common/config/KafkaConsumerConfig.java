package com.example.order.common.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${kafka.host}")
    private String host;

    @Value("${kafka.port}")
    private int port;

    public static final String KAFKA_BROKER_URL = "%s:%d";

    public static final String PAYMENT_CONFIRMED_EVENT_CONSUMER_GROUP_ID = "order-payment-confirmed-event-consumer";
    public static final String PAYMENT_FAILED_EVENT_CONSUMER_GROUP_ID = "order-payment-failed-event-consumer";

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, String.format(KAFKA_BROKER_URL, host, port));
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(configs);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> paymentConfirmedEventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setGroupId(PAYMENT_CONFIRMED_EVENT_CONSUMER_GROUP_ID);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> paymentFailedEventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setGroupId(PAYMENT_FAILED_EVENT_CONSUMER_GROUP_ID);
        return factory;
    }
}
