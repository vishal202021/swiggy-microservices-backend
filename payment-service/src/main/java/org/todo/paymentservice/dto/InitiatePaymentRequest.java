package org.todo.paymentservice.dto;

import lombok.Data;

@Data
public class InitiatePaymentRequest {
    private Long orderId;
    private String provider;
}
