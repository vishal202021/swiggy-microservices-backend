package com.example.deliveryservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "delivery_partners")
public class DeliveryPartner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;

    @Enumerated(EnumType.STRING)
    private PartnerStatus status;


}
