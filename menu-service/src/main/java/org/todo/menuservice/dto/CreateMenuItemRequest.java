package org.todo.menuservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateMenuItemRequest {
    private Long restaurantId;
    private String name;
    private String description;
    private BigDecimal price;
}
