package org.todo.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.todo.restaurantservice.dto.CreateRestaurantRequest;
import org.todo.restaurantservice.dto.RestaurantResponse;
import org.todo.restaurantservice.helper.ToRestaurantResponse;
import org.todo.restaurantservice.model.Restaurant;
import org.todo.restaurantservice.repo.RestaurantRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantResponse create(Long ownerId, CreateRestaurantRequest createRestaurantRequest) {
        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(ownerId);
        restaurant.setName(createRestaurantRequest.getName());
        restaurant.setAddress(createRestaurantRequest.getAddress());
        restaurant.setDescription(createRestaurantRequest.getDescription());
        restaurant.setCuisine(createRestaurantRequest.getCuisine());
        restaurant.setRating(BigDecimal.ZERO);
        restaurant.setIsOpen(true);
        restaurant.setCreatedAt(LocalDateTime.now());
        restaurant.setUpdateAt(LocalDateTime.now());
        restaurantRepository.save(restaurant);
        return ToRestaurantResponse.toRestaurantResponse(restaurant);
    }

    public List<RestaurantResponse> getOpenRestaurants() {
       List<Restaurant> restaurants = restaurantRepository.findByIsOpenTrue();
       List<RestaurantResponse> restaurantResponseList = new ArrayList<>();
       for (Restaurant restaurant : restaurants) {
           RestaurantResponse restaurantResponse = ToRestaurantResponse.toRestaurantResponse(restaurant);
           restaurantResponseList.add(restaurantResponse);
       }
       return restaurantResponseList;
    }

    public List<RestaurantResponse> getByOwnerId(Long ownerId) {
        List<Restaurant> restaurants = restaurantRepository.findByOwnerId(ownerId);
        List<RestaurantResponse> restaurantResponseList = new ArrayList<>();
        for(Restaurant restaurant : restaurants){
            RestaurantResponse restaurantResponse=ToRestaurantResponse.toRestaurantResponse(restaurant);
            restaurantResponseList.add(restaurantResponse);
        }
        return restaurantResponseList;
    }

   public Long getOwnerId(Long restaurantId){
       Restaurant restaurant = restaurantRepository.findById(restaurantId)
               .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Restaurant not found"));
       return restaurant.getOwnerId();
   }
}
