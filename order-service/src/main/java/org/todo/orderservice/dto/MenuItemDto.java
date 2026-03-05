package org.todo.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuItemDto {
    private Long id;
    private Long restaurantId;
    private BigDecimal price;
    private Boolean available;
}
