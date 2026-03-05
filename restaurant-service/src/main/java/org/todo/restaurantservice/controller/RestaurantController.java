package org.todo.restaurantservice.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.todo.restaurantservice.dto.CreateRestaurantRequest;
import org.todo.restaurantservice.dto.RestaurantResponse;
import org.todo.restaurantservice.service.RestaurantService;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(@RequestHeader("X-User-Id")Long userId, @RequestBody CreateRestaurantRequest createRestaurantRequest){
        RestaurantResponse restaurantResponse=restaurantService.create(userId, createRestaurantRequest);
        return ResponseEntity.ok().body(restaurantResponse);
    }

    @GetMapping("/open")
    public ResponseEntity<List<RestaurantResponse>> getOpenRestaurants(){
        return ResponseEntity.ok().body(restaurantService.getOpenRestaurants());
    }

    @GetMapping("/owner")
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok().body(restaurantService.getByOwnerId(userId));
    }

    @GetMapping("/{id}/owner")
    public ResponseEntity<Long> getRestaurantOwner(@PathVariable Long id){
        return ResponseEntity.ok().body(restaurantService.getOwnerId(id));
    }



}
