package org.todo.orderservice.outbox;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.todo.orderservice.Kafka.OrderEventProducer;
import org.todo.orderservice.dto.OutboxEvent;
import org.todo.orderservice.repo.OutBoxRepo;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);

    private final OutBoxRepo outBoxRepo;
    private final OrderEventProducer orderEventProducer;

    @Scheduled(fixedDelay = 3000)
    public void publishOutboxEvents() {

        List<OutboxEvent> pendingEvents = outBoxRepo.findByStatus("PENDING");

        for (OutboxEvent event : pendingEvents) {
            try {
                orderEventProducer.send(event.getPayload());

                event.setStatus("PUBLISHED");
                outBoxRepo.save(event);

            } catch (Exception ex) {
                log.error("Failed to publish outbox event {}", event.getId(), ex);
            }
        }
    }
}
