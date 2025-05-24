package com.mealdeals.meal_deals.repository;

import com.mealdeals.meal_deals.entity.Order;
import com.mealdeals.meal_deals.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
