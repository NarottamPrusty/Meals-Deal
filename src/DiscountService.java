package com.mealdeals.meal_deals.security.service;

import com.mealdeals.meal_deals.dto.CreateDiscountRequest;
import com.mealdeals.meal_deals.dto.DiscountDTO;
import com.mealdeals.meal_deals.entity.Discount;
import com.mealdeals.meal_deals.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;

    public DiscountDTO createDiscount(CreateDiscountRequest request) {
        Discount discount = Discount.builder()
                .code(request.getCode().toUpperCase())
                .percentage(request.getPercentage())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .active(true)
                .build();

        discountRepository.save(discount);

        return new DiscountDTO(discount.getCode(), discount.getPercentage(),
                discount.getStartDate(), discount.getEndDate());
    }

    public List<DiscountDTO> getAllDiscounts() {
        return discountRepository.findAll().stream()
                .map(d -> new DiscountDTO(d.getCode(), d.getPercentage(), d.getStartDate(), d.getEndDate()))
                .collect(Collectors.toList());
    }

    public Discount validateDiscount(String code) {
        return discountRepository.findByCode(code.toUpperCase())
                .filter(Discount::isActive)
                .filter(d -> d.getStartDate().isBefore(java.time.LocalDateTime.now()))
                .filter(d -> d.getEndDate().isAfter(java.time.LocalDateTime.now()))
                .orElseThrow(() -> new RuntimeException("Invalid or expired discount code"));
    }
}