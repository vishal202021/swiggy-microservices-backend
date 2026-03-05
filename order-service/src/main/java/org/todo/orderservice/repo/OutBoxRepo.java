package org.todo.orderservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.todo.orderservice.dto.OutboxEvent;

import java.util.List;
import java.util.UUID;

public interface OutBoxRepo extends JpaRepository<OutboxEvent, UUID> {
    List<OutboxEvent> findByStatus(String status);
}
