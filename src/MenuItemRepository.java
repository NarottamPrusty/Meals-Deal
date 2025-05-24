package com.mealdeals.meal_deals.repository;

import com.mealdeals.meal_deals.entity.MenuItem;
import com.mealdeals.meal_deals.entity.Restaurant;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByRestaurantId(Restaurant restaurant);
}
