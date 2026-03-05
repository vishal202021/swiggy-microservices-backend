package org.todo.orderservice.dto;

import lombok.Data;
import org.todo.orderservice.model.OrderStatus;

import java.math.BigDecimal;

@Data
public class OrderResponse {
    private Long orderId;
    private Long restaurantId;
    private BigDecimal totalAmount;
    private OrderStatus status;
}
