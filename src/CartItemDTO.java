package com.mealdeals.meal_deals.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CartItemDTO {
    private Long id;
    private Long menuItemId;
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;
}
