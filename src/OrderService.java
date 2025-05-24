package com.mealdeals.meal_deals.security.service;

import com.mealdeals.meal_deals.dto.MenuItemDTO;
import com.mealdeals.meal_deals.dto.OrderDTO;
import com.mealdeals.meal_deals.entity.*;
import com.mealdeals.meal_deals.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final DiscountService discountService;
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final CartService cartService;
    private final RestaurantRepository restaurantRepository;

    private static final BigDecimal DISCOUNT_THRESHOLD = new BigDecimal("50.00");
    private static final BigDecimal DISCOUNT_PERCENTAGE = new BigDecimal("0.10");

    public OrderDTO placeOrder(Long userId, String discountCode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    
        List<MenuItem> cartItems = cartService.getCartItems(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
    
        BigDecimal originalTotal = cartItems.stream()
                .map(MenuItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    
        // 🆕 Discount logic
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (discountCode != null && !discountCode.isBlank()) {
            Discount discount = discountService.validateDiscount(discountCode);
            discountAmount = originalTotal.multiply(discount.getPercentage());
        }
    
        BigDecimal finalAmount = originalTotal.subtract(discountAmount);
    
        Order order = new Order();
        order.setUser(user);
        order.setItems(cartItems);
        order.setTotalAmount(originalTotal);
        order.setDiscountAmount(discountAmount);
        order.setFinalAmount(finalAmount);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
    
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(userId);
    
        return toDTO(savedOrder, originalTotal, discountAmount, finalAmount);
    }
    

    public String processPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            return "Order has already been paid or is not in 'Pending' status.";
        }

        order.setStatus(OrderStatus.PREPARING);
        orderRepository.save(order);

        return "Payment successful for order ID " + orderId;
    }

    public List<OrderDTO> getOrdersByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUser(user).stream()
                .map(order -> toDTO(order, order.getTotalAmount(), order.getDiscountAmount(), order.getFinalAmount()))
                .collect(Collectors.toList());
    }

    public String trackOrderStatus(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (userId != null && !order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        return order.getStatus().name();
    }

    public String cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized to cancel this order.");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only pending orders can be cancelled.");
        }

        if (order.getCreatedAt().plusMinutes(5).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Order cannot be cancelled after 5 minutes.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        return "Order cancelled successfully.";
    }

    public OrderDTO getOrderDetails(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        return toDTO(order, order.getTotalAmount(), order.getDiscountAmount(), order.getFinalAmount());
    }

    public List<OrderDTO> getOrdersByRestaurant(Long restaurantId, String requestingUserEmail) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        if (!restaurant.getOwner().getEmail().equals(requestingUserEmail)) {
            throw new RuntimeException("Unauthorized access to orders of this restaurant.");
        }

        return orderRepository.findAll().stream()
                .filter(order -> order.getItems().stream()
                        .allMatch(item -> item.getRestaurant().getId().equals(restaurantId)))
                .map(order -> toDTO(order, order.getTotalAmount(), order.getDiscountAmount(), order.getFinalAmount()))
                .collect(Collectors.toList());
    }

    private BigDecimal calculateDiscount(BigDecimal originalTotal) {
        return originalTotal.compareTo(DISCOUNT_THRESHOLD) > 0
                ? originalTotal.multiply(DISCOUNT_PERCENTAGE)
                : BigDecimal.ZERO;
    }

    private OrderDTO toDTO(Order order, BigDecimal originalTotal, BigDecimal discount, BigDecimal finalAmount) {
        List<MenuItemDTO> items = order.getItems().stream()
                .map(item -> new MenuItemDTO(
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getPrice(),
                        item.getRestaurant().getId()
                ))
                .collect(Collectors.toList());

        return new OrderDTO(
                order.getId(),
                order.getUser().getId(),
                items,
                originalTotal,
                discount,
                finalAmount,
                order.getStatus().name()
        );
    }
}
