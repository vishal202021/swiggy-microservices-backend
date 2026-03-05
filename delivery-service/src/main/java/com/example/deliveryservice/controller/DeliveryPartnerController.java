package com.example.deliveryservice.controller;

import com.example.deliveryservice.model.DeliveryPartner;
import com.example.deliveryservice.model.PartnerStatus;
import com.example.deliveryservice.repo.DeliveryPartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/partners")
@RestController
@RequiredArgsConstructor
public class DeliveryPartnerController {

    private final DeliveryPartnerRepository repository;

    @PostMapping
    public DeliveryPartner add(@RequestBody DeliveryPartner deliveryPartner){
        deliveryPartner.setStatus(PartnerStatus.AVAILABLE);
        return repository.save(deliveryPartner);
    }

    @GetMapping("/available")
    public List<DeliveryPartner> available(){
       return repository.findAll()
                .stream()
                .filter(p->p.getStatus()==PartnerStatus.AVAILABLE)
                .toList();
    }
}
