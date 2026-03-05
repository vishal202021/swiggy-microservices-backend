package org.todo.orderservice.helper;

import lombok.Data;
import org.todo.orderservice.dto.OrderDto;
import org.todo.orderservice.model.Orders;

@Data
public class ToOrderDTO {
    public static OrderDto toOrderDto(Orders order) {
        OrderDto orderDto=new OrderDto();
        orderDto.setOrderId(order.getId());
        orderDto.setUserId(order.getUserId());
        orderDto.setRestaurantId(order.getRestaurantId());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setStatus(order.getStatus());
        return orderDto;
    }
}
