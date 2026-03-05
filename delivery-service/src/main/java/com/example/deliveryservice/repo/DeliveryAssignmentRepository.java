package com.example.deliveryservice.repo;

import com.example.deliveryservice.model.DeliveryAssignment;
import com.example.deliveryservice.model.DeliveryPartner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryAssignmentRepository extends JpaRepository<DeliveryAssignment,Long> {
    Optional<DeliveryAssignment> findByOrderId(Long orderId);
}
