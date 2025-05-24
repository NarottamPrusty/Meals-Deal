package com.mealdeals.meal_deals.controller;

import com.mealdeals.meal_deals.dto.CartDTO;
import com.mealdeals.meal_deals.dto.CartItemRequest;
import com.mealdeals.meal_deals.security.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addItemToCart(@Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addItemToCart(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @DeleteMapping("/{userId}/remove/{menuItemId}")
    public ResponseEntity<CartDTO> removeItemFromCart(@PathVariable Long userId, @PathVariable Long menuItemId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(userId, menuItemId));
    }
}
