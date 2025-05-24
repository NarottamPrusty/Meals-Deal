package com.mealdeals.meal_deals.controller;

import com.mealdeals.meal_deals.dto.MenuItemDTO;
import com.mealdeals.meal_deals.dto.CreateMenuItemRequest;
import com.mealdeals.meal_deals.security.service.MenuItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDTO> getMenuItemById(@PathVariable Long id) {
        MenuItemDTO itemDTO = menuItemService.getMenuItemById(id);
        return new ResponseEntity<>(itemDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDTO> updateMenuItem(@PathVariable Long id, @Valid @RequestBody CreateMenuItemRequest request) {
        MenuItemDTO updated = menuItemService.updateMenuItem(id, request);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem( @PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
