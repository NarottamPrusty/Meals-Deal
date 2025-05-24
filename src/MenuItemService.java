package com.mealdeals.meal_deals.security.service;

import com.mealdeals.meal_deals.dto.CreateMenuItemRequest;
import com.mealdeals.meal_deals.dto.MenuItemDTO;
import com.mealdeals.meal_deals.entity.MenuItem;
import com.mealdeals.meal_deals.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemDTO getMenuItemById(Long id) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        return new MenuItemDTO(item.getId(), item.getName(), item.getDescription(), item.getPrice(), item.getRestaurant().getId());
    }

    public MenuItemDTO updateMenuItem(Long id, CreateMenuItemRequest request) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());

        MenuItem updated = menuItemRepository.save(item);

        return new MenuItemDTO(updated.getId(), updated.getName(), updated.getDescription(), updated.getPrice(), updated.getRestaurant().getId());
    }

    public void deleteMenuItem(Long id) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));
        menuItemRepository.delete(item);
    }
}
