package org.todo.paymentservice.dto;

import lombok.Data;
import org.todo.paymentservice.model.PaymentStatus;

import java.math.BigDecimal;

@Data
public class PaymentResponse {
    private Long paymentId;
    private Long orderId;
    private BigDecimal amount;
    private PaymentStatus status;
    private String razorpayOrderId;
    private String razorpayKey;
}
