package org.todo.restaurantservice.dto;

import lombok.Data;

@Data
public class CreateRestaurantRequest {
    private String name;
    private String description;
    private String address;
    private String cuisine;
}
