package com.mealdeals.meal_deals.repository;

import com.mealdeals.meal_deals.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
