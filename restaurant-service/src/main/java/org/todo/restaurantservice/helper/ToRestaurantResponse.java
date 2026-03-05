package org.todo.restaurantservice.helper;

import lombok.Data;
import org.todo.restaurantservice.dto.RestaurantResponse;
import org.todo.restaurantservice.model.Restaurant;

@Data
public class ToRestaurantResponse {

    public static RestaurantResponse toRestaurantResponse(Restaurant restaurant) {
        RestaurantResponse restaurantResponse = new RestaurantResponse();
        restaurantResponse.setId(restaurant.getId());
        restaurantResponse.setName(restaurant.getName());
        restaurantResponse.setAddress(restaurant.getAddress());
        restaurantResponse.setCuisine(restaurant.getCuisine());
        restaurantResponse.setRating(restaurant.getRating());
        restaurantResponse.setIsOpen(restaurant.getIsOpen());
        return restaurantResponse;
    }
}
