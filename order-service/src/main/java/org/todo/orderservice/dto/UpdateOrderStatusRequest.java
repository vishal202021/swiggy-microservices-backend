package org.todo.orderservice.dto;

import lombok.Data;
import org.todo.orderservice.model.OrderStatus;

@Data
public class UpdateOrderStatusRequest {
    private OrderStatus status;
}
