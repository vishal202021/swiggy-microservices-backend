package org.todo.menuservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.todo.menuservice.dto.CreateMenuItemRequest;
import org.todo.menuservice.dto.MenuItemResponse;
import org.todo.menuservice.service.MenuService;

import java.util.List;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<MenuItemResponse> create(
            @RequestBody CreateMenuItemRequest createMenuItemRequest
    ){
        return ResponseEntity.ok().body(menuService.create(createMenuItemRequest));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItemResponse>> getMenu(
            @PathVariable Long restaurantId) {

        return ResponseEntity.ok(menuService.getMenuByRestaurant(restaurantId));
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<MenuItemResponse> updateAvailability(
            @PathVariable Long id,
            @RequestParam Boolean available) {

        return ResponseEntity.ok(menuService.updateAvailability(id, available));
    }
    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponse> getMenuItemById(
            @PathVariable Long id) {

        return ResponseEntity.ok(menuService.getById(id));
    }
}
