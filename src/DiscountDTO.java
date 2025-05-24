package com.mealdeals.meal_deals.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDTO {
    private String code;
    private BigDecimal percentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
