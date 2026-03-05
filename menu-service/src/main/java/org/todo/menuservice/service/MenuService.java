package org.todo.menuservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.todo.menuservice.dto.CreateMenuItemRequest;
import org.todo.menuservice.dto.MenuItemResponse;
import org.todo.menuservice.helper.ToResponse;
import org.todo.menuservice.model.MenuItem;
import org.todo.menuservice.repo.MenuRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.todo.menuservice.helper.ToResponse.toResponse;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepo menuRepo;

    public MenuItemResponse create(CreateMenuItemRequest createMenuItemRequest) {
        MenuItem item = new MenuItem();
        item.setRestaurantId(createMenuItemRequest.getRestaurantId());
        item.setName(createMenuItemRequest.getName());
        item.setDescription(createMenuItemRequest.getDescription());
        item.setPrice(createMenuItemRequest.getPrice());
        item.setAvailable(true);
        item.setCreateAt(LocalDateTime.now());
        item.setUpdateAt(LocalDateTime.now());
        MenuItem saved= menuRepo.save(item);
        return toResponse(saved);
    }

    public List<MenuItemResponse> getMenuByRestaurant(Long restaurantId) {
        List<MenuItem> items=menuRepo.findByRestaurantIdAndAvailableTrue(restaurantId);
        List<MenuItemResponse> menuResponse=new ArrayList<>();
        for(MenuItem item:items){
          MenuItemResponse itemResponse= toResponse(item);
          menuResponse.add(itemResponse);
        }
      return menuResponse;
    }
    public MenuItemResponse updateAvailability(Long id, Boolean available){
        MenuItem item=menuRepo.findById(id).orElseThrow(()->new RuntimeException("Menu item not found"));
        item.setAvailable(available);
        item.setUpdateAt(LocalDateTime.now());
        MenuItem saved=menuRepo.save(item);
        return toResponse(saved);
    }

    public MenuItemResponse getById(Long id) {

        MenuItem item = menuRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Menu item not found"));

        return toResponse(item);
    }



}
