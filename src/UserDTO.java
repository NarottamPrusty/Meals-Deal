package com.mealdeals.meal_deals.dto;

import com.mealdeals.meal_deals.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private Set<Role> roles;
}
