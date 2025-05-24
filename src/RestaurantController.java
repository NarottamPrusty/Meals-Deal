package com.mealdeals.meal_deals.controller;

import com.mealdeals.meal_deals.security.service.RestaurantService;

import jakarta.validation.Valid;

import com.mealdeals.meal_deals.dto.CreateRestaurantRequest;
import com.mealdeals.meal_deals.dto.MenuItemDTO;
import com.mealdeals.meal_deals.dto.RestaurantDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

   @PutMapping("/{id}")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@PathVariable Long id, @Valid @RequestBody CreateRestaurantRequest request) {
        RestaurantDTO updated = restaurantService.updateRestaurant(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<List<MenuItemDTO>> getRestaurantMenu(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getMenuByRestaurant(id));
    }

}
