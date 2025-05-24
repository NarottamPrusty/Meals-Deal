package com.mealdeals.meal_deals.repository;

import com.mealdeals.meal_deals.entity.Cart;
import com.mealdeals.meal_deals.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
