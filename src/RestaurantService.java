package com.mealdeals.meal_deals.security.service;

import com.mealdeals.meal_deals.dto.*;
import com.mealdeals.meal_deals.entity.*;
import com.mealdeals.meal_deals.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    public RestaurantDTO createRestaurant(CreateRestaurantRequest requestDto) {
        String userEmail = getCurrentUserEmail();
        User owner = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Restaurant restaurant = new Restaurant();
        restaurant.setName(requestDto.getName());
        restaurant.setAddress(requestDto.getAddress());
        restaurant.setOwner(owner);

        Restaurant saved = restaurantRepository.save(restaurant);
        return new RestaurantDTO(saved.getId(), saved.getName(), saved.getAddress());
    }

    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(r -> new RestaurantDTO(r.getId(), r.getName(), r.getAddress()))
                .collect(Collectors.toList());
    }

    public MenuItemDTO addMenuItemToRestaurant(Long restaurantId, CreateMenuItemRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        MenuItem item = new MenuItem();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setRestaurant(restaurant);

        MenuItem saved = menuItemRepository.save(item);
        return new MenuItemDTO(saved.getId(), saved.getName(), saved.getDescription(), saved.getPrice(), restaurantId);
    }

    public List<MenuItemDTO> getMenuByRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        return menuItemRepository.findByRestaurantId(restaurant).stream()
                .map(item -> new MenuItemDTO(
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getPrice(),
                        restaurantId
                )).collect(Collectors.toList());
    }

    public RestaurantDTO getRestaurantById(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        return new RestaurantDTO(restaurant.getId(), restaurant.getName(), restaurant.getAddress());
    }

    public RestaurantDTO updateRestaurant(Long id, CreateRestaurantRequest request) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        validateOwnership(restaurant);

        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        Restaurant updated = restaurantRepository.save(restaurant);
        return new RestaurantDTO(updated.getId(), updated.getName(), updated.getAddress());
    }

    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        validateOwnership(restaurant);
        restaurantRepository.delete(restaurant);
    }

    private void validateOwnership(Restaurant restaurant) {
        String userEmail = getCurrentUserEmail();
        if (!restaurant.getOwner().getEmail().equals(userEmail)) {
            throw new RuntimeException("You do not own this restaurant.");
        }
    }

    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
