package org.todo.notificationservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.todo.notificationservice.event.OrderStatusEvent;
import org.todo.notificationservice.model.Notification;
import org.todo.notificationservice.model.ProcessedEvent;
import org.todo.notificationservice.repo.NotificationRepository;
import org.todo.notificationservice.repo.ProcessedEventRepo;
import org.todo.notificationservice.service.NotificationChannelService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final NotificationRepository notificationRepository;
    private final NotificationChannelService notificationChannelService;
    private final ProcessedEventRepo processedEventRepo;

    private static final Logger log =
            LoggerFactory.getLogger(OrderEventConsumer.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(
            topics = "order-created",
            groupId = "notification-service-v2"
    )
    public void consume(String payload) {

        try {

            OrderStatusEvent event =
                    objectMapper.readValue(payload, OrderStatusEvent.class);


            if (processedEventRepo.existsById(event.getEventId())) {
                return;
            }


            Notification notification = Notification.builder()
                    .userId(event.getUserId())
                    .title("Order Update: " + event.getStatus())
                    .message("Your order #" + event.getOrderId()
                            + " status is " + event.getStatus())
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);


            notificationChannelService.sendOrderCreatedNotification(
                    event.getUserId(),
                    event.getOrderId(),
                    event.getStatus()
            );


            processedEventRepo.save(
                    new ProcessedEvent(event.getEventId())
            );

            log.info("Notification processed for eventId={}", event.getEventId());

        } catch (Exception e) {
            log.error("Notification delivery failed", e);
            throw new RuntimeException(e);
        }
    }
}
