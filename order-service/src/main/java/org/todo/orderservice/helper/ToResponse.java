package org.todo.orderservice.helper;
import lombok.Data;
import org.todo.orderservice.dto.OrderResponse;
import org.todo.orderservice.model.Orders;

@Data
public class ToResponse {
    public static OrderResponse toOrderResponse(Orders order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getId());
        orderResponse.setRestaurantId(order.getRestaurantId());
        orderResponse.setTotalAmount(order.getTotalAmount());
        orderResponse.setStatus(order.getStatus());
        return orderResponse;
    }
}
