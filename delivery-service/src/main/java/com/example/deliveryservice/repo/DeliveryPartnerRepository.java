package com.example.deliveryservice.repo;

import com.example.deliveryservice.model.DeliveryAssignment;
import com.example.deliveryservice.model.DeliveryPartner;
import com.example.deliveryservice.model.PartnerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryPartnerRepository extends JpaRepository<DeliveryPartner,Long> {
    Optional<DeliveryPartner> findFirstByStatus(PartnerStatus status);
}
