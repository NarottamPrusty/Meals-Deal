package com.mealdeals.meal_deals.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderDTO {

    private Long id;
    private Long userId;
    private List<MenuItemDTO> items;
    private BigDecimal originalTotal;
    private BigDecimal discount;
    private BigDecimal finalAmount;
    private String status;

    // Updated constructor with all fields
    public OrderDTO(Long id, Long userId, List<MenuItemDTO> items, BigDecimal originalTotal, BigDecimal discount, BigDecimal finalAmount, String status) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.originalTotal = originalTotal;
        this.discount = discount;
        this.finalAmount = finalAmount;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<MenuItemDTO> getItems() {
        return items;
    }

    public void setItems(List<MenuItemDTO> items) {
        this.items = items;
    }

    public BigDecimal getOriginalTotal() {
        return originalTotal;
    }

    public void setOriginalTotal(BigDecimal originalTotal) {
        this.originalTotal = originalTotal;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
