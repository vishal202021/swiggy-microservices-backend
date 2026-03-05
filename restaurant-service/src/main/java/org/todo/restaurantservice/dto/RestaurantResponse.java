package org.todo.restaurantservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RestaurantResponse {
    private Long id;
    private String name;
    private String address;
    private String cuisine;
    private BigDecimal rating;
    private Boolean isOpen;
}
