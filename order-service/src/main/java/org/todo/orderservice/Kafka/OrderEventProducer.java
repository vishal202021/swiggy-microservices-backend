package org.todo.orderservice.Kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.todo.orderservice.dto.OrderStatusEvent;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, Object>  kafkaTemplate;

    private static final String TOPIC = "order-created";

    public void send(String payload) {
        kafkaTemplate.send(TOPIC, payload);
    }
}
