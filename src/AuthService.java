package com.mealdeals.meal_deals.security.service;

import com.mealdeals.meal_deals.dto.AuthResponse;
import com.mealdeals.meal_deals.dto.LoginRequest;
import com.mealdeals.meal_deals.dto.RegisterRequest;
import com.mealdeals.meal_deals.entity.User;
import com.mealdeals.meal_deals.security.jwt.JwtService;
import com.mealdeals.meal_deals.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ResponseEntity<String> register(RegisterRequest registerRequest) {
        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }

        // Create and save new user
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRoles(registerRequest.getRoles());

        userRepository.save(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    public ResponseEntity<AuthResponse> login(LoginRequest loginRequest) {
        // Fetch the user from the repository using email
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if password matches
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Generate JWT token using the user object
        String token = jwtService.generateToken(user);

        // Return the token in the response
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
