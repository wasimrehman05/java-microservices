package com.bankai.userservice.controller;

import com.bankai.userservice.dto.UserRequest;
import com.bankai.userservice.dto.UserResponse;
import com.bankai.userservice.entity.User;
import com.bankai.userservice.mapper.UserMapper;
import com.bankai.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/internal")
@RequiredArgsConstructor
public class InternalController {
    private final UserService userService;

    /**
     * Internal endpoint for auth-service to create user profiles
     * This endpoint is called after successful user registration in auth-service
     */
    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUserFromAuth(@RequestBody CreateUserFromAuthRequest request) {
        UserRequest userRequest = UserRequest.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();

        User user = UserMapper.toUser(userRequest);
        UserResponse userResponse = userService.createUser(user);
        
        return ResponseEntity
                .created(URI.create("/api/users/" + userResponse.getId()))
                .body(userResponse);
    }

    /**
     * Internal endpoint to check if user exists by email
     */
    @GetMapping("/users/exists")
    public ResponseEntity<Boolean> userExists(@RequestParam String email) {
        try {
            // This will throw UserNotFoundException if not found
            userService.getUserByEmail(email);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    /**
     * DTO for creating user from auth service
     */
    public static class CreateUserFromAuthRequest {
        private String name;
        private String email;
        private String phone;

        // Constructors
        public CreateUserFromAuthRequest() {}

        public CreateUserFromAuthRequest(String name, String email, String phone) {
            this.name = name;
            this.email = email;
            this.phone = phone;
        }

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
    }
} 