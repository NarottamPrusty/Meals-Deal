package com.mealdeals.meal_deals.controller;

import com.mealdeals.meal_deals.dto.CreateDiscountRequest;
import com.mealdeals.meal_deals.dto.DiscountDTO;
import com.mealdeals.meal_deals.security.service.DiscountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @PostMapping
    public ResponseEntity<DiscountDTO> createDiscount(@Valid @RequestBody CreateDiscountRequest request) {
        return ResponseEntity.ok(discountService.createDiscount(request));
    }

    @GetMapping
    public ResponseEntity<List<DiscountDTO>> getAllDiscounts() {
        return ResponseEntity.ok(discountService.getAllDiscounts());
    }
}
