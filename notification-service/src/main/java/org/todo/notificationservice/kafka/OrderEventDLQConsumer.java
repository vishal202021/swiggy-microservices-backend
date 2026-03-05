package org.todo.notificationservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderEventDLQConsumer {

    @KafkaListener(
            topics = "order-created-dl1",
            groupId = "notification-service-v2"
    )

    public void consumeDlq(String payload){
        log.error("DQL Event Receive");
        log.error("Payload: {}",payload);
    }
}
