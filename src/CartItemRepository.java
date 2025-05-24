package com.mealdeals.meal_deals.repository;

import com.mealdeals.meal_deals.entity.CartItem;
import com.mealdeals.meal_deals.entity.Cart;
import com.mealdeals.meal_deals.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndMenuItem(Cart cart, MenuItem menuItem);
}
