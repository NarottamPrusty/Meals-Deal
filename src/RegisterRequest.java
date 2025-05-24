package com.mealdeals.meal_deals.dto;

import com.mealdeals.meal_deals.entity.Role;
import lombok.*;

import java.util.Set;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Set<Role> roles; // e.g., [CUSTOMER]
}
