package com.mealdeals.meal_deals.controller;

import com.mealdeals.meal_deals.dto.OrderDTO;
import com.mealdeals.meal_deals.security.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // ✅ Place Order with optional discount code
    @PostMapping("/{userId}/checkout")
    public ResponseEntity<OrderDTO> placeOrder(
            @PathVariable Long userId,
            @RequestParam(required = false) String discountCode) {
        return ResponseEntity.ok(orderService.placeOrder(userId, discountCode));
    }

    // ✅ Process payment
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<String> processPayment(@Valid @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.processPayment(orderId));
    }

    // ✅ Get orders placed by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getUserOrders(@Valid @PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    // ✅ Track order status
    @GetMapping("/{orderId}/status")
    public ResponseEntity<String> getOrderStatus( @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.trackOrderStatus(null, orderId)); // tracking open to all
    }

    // ✅ Cancel order
    @PutMapping("/{userId}/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder( @PathVariable Long userId, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(userId, orderId));
    }

    // ✅ View specific order details for a user
    @GetMapping("/user/{userId}/details/{orderId}")
    public ResponseEntity<OrderDTO> getOrderDetails(@PathVariable Long userId, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderDetails(userId, orderId));
    }

    // ✅ Restaurant owner views orders for their restaurant
    @GetMapping("/restaurant/{restaurantId}")
    @RequestMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<OrderDTO>> getRestaurantOrders(@PathVariable Long restaurantId) {
        String email = getCurrentUserEmail();
        return ResponseEntity.ok(orderService.getOrdersByRestaurant(restaurantId, email));
    }

    // ✅ Utility to get logged-in email
    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else {
            return principal.toString();
        }
    }
}
