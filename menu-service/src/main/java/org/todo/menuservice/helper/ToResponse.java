package org.todo.menuservice.helper;

import org.todo.menuservice.dto.MenuItemResponse;
import org.todo.menuservice.model.MenuItem;

public class ToResponse {

    public static MenuItemResponse toResponse(MenuItem menuItem){
        MenuItemResponse menuItemResponse = new MenuItemResponse();
        menuItemResponse.setId(menuItem.getId());
        menuItemResponse.setRestaurantId(menuItem.getRestaurantId());
        menuItemResponse.setName(menuItem.getName());
        menuItemResponse.setDescription(menuItem.getDescription());
        menuItemResponse.setPrice(menuItem.getPrice());
        menuItemResponse.setAvailable(menuItem.getAvailable());
        return menuItemResponse;
    }
}
