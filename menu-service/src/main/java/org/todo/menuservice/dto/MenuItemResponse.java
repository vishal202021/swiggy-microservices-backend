package org.todo.menuservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuItemResponse {
    private Long id;
    private Long restaurantId;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean available;
}
