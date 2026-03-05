package org.todo.notificationservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "processed_events")
@AllArgsConstructor
@NoArgsConstructor
public class ProcessedEvent {

    @Id
    private String eventId;

    public ProcessedEvent(String eventId) {
        this.eventId = eventId;
    }

    private LocalDateTime processedAt = LocalDateTime.now();
}

