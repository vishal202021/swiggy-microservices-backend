package org.todo.orderservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlaceOrderRequest {
    private Long restaurantId;
    private List<OrderItemRequest> items;
}
