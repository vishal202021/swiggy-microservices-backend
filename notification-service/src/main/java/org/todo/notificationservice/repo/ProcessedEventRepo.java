package org.todo.notificationservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.todo.notificationservice.model.ProcessedEvent;

@Repository
public interface ProcessedEventRepo extends JpaRepository<ProcessedEvent,String> {
}
