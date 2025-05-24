package com.mealdeals.meal_deals.security.service;

import com.mealdeals.meal_deals.entity.User;
import com.mealdeals.meal_deals.entity.Role;
import com.mealdeals.meal_deals.repository.UserRepository;
import com.mealdeals.meal_deals.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // Get user by ID
    public Optional<User> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            logger.info("User retrieved with ID: {}", id);
        } else {
            logger.warn("User not found with ID: {}", id);
        }
        return user;
    }

    // Get all users
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        logger.info("Retrieved all users, total count: {}", users.size());
        return users;
    }

    // Delete user by ID
    @Transactional
    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            logger.error("Attempted to delete non-existent user with ID: {}", id);
            throw new UserNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
        logger.info("User deleted with ID: {}", id);
    }

    // Update user roles
    @Transactional
    public void updateUserRoles(Long userId, Set<String> roleNames) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            logger.error("User not found with ID: {}", userId);
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }

        User user = optionalUser.get();
        Set<Role> roles = roleNames.stream()
                .map(roleName -> Role.valueOf(roleName.toUpperCase()))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        userRepository.save(user);

        logger.info("Updated roles for user with ID: {}", userId);
    }
}
