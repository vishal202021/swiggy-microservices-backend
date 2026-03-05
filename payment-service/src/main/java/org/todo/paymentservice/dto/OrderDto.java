package org.todo.paymentservice.dto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderDto {
    private Long orderId;
    private Long userId;
    private Long restaurantId;
    private BigDecimal totalAmount;
    private OrderStatus status;
}
