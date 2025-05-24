package com.mealdeals.meal_deals.security.service;

import com.mealdeals.meal_deals.dto.CartDTO;
import com.mealdeals.meal_deals.dto.CartItemDTO;
import com.mealdeals.meal_deals.repository.CartRepository;
import com.mealdeals.meal_deals.repository.CartItemRepository;
import com.mealdeals.meal_deals.dto.CartItemRequest;
import com.mealdeals.meal_deals.dto.MenuItemDTO;
import com.mealdeals.meal_deals.entity.Cart;
import com.mealdeals.meal_deals.entity.CartItem;
import com.mealdeals.meal_deals.entity.MenuItem;
import com.mealdeals.meal_deals.entity.User;
import com.mealdeals.meal_deals.repository.MenuItemRepository;
import com.mealdeals.meal_deals.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartDTO addItemToCart(CartItemRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        // Enforce single-restaurant rule
        if (cart.getItems() != null && !cart.getItems().isEmpty()) {
            Long existingRestaurantId = cart.getItems().get(0).getMenuItem().getRestaurant().getId();
            if (!menuItem.getRestaurant().getId().equals(existingRestaurantId)) {
                throw new RuntimeException("Cannot add items from multiple restaurants in a single cart.");
            }
        }

        CartItem cartItem = cartItemRepository.findByCartAndMenuItem(cart, menuItem).orElse(null);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .menuItem(menuItem)
                    .quantity(1)
                    .build();
        }
        cartItemRepository.save(cartItem);

        return buildCartDTO(cart);
    }

    public CartDTO getCartByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));
        return buildCartDTO(cart);
    }

    public CartDTO removeItemFromCart(Long userId, Long menuItemId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().removeIf(item -> item.getMenuItem().getId().equals(menuItemId));
        cartRepository.save(cart);

        return buildCartDTO(cart);
    }

    public List<MenuItem> getCartItems(Long userId) {
        CartDTO cartDTO = getCartByUserId(userId);
        return cartDTO.getItems().stream()
                .map(item -> menuItemRepository.findById(item.getMenuItemId()).orElseThrow())
                .toList();
    }

    public void clearCart(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Cart cart = cartRepository.findByUser(user).orElseThrow();
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private CartDTO buildCartDTO(Cart cart) {
        List<CartItemDTO> itemDTOs = cart.getItems().stream().map(item -> {
            MenuItem m = item.getMenuItem();
            return new CartItemDTO(
                    item.getId(),
                    m.getId(),
                    m.getName(),
                    m.getDescription(),
                    m.getPrice(),
                    item.getQuantity()
            );
        }).toList();

        BigDecimal total = cart.getItems().stream()
                .map(i -> i.getMenuItem().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartDTO(cart.getUser().getId(), itemDTOs, total);
    }
}
