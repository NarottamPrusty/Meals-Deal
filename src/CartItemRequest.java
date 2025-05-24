package com.mealdeals.meal_deals.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long userId;
    private Long menuItemId;
}
