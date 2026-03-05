package org.todo.orderservice.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
@Data
public class OutboxEvent {

    @Id
    private UUID id;

    private Long aggregateId;
    private String eventType;

    @Lob
    private String payload;

    private String status;
    private LocalDateTime createdAt;


    public static OutboxEvent from(OrderStatusEvent event) {

        OutboxEvent outbox = new OutboxEvent();
        outbox.setId(UUID.fromString(event.getEventId()));
        outbox.setAggregateId(event.getOrderId());
        outbox.setEventType("ORDER_STATUS_CHANGED");
        outbox.setPayload(convertToJson(event));
        outbox.setStatus("PENDING");
        outbox.setCreatedAt(LocalDateTime.now());

        return outbox;
    }

    private static String convertToJson(OrderStatusEvent event) {
        try {
            return new ObjectMapper().writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize OrderStatusEvent", e);
        }
    }
}
