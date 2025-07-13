package com.bankai.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceClient {
    private final WebClient.Builder webClientBuilder;
    
    @Value("${user-service.url:http://localhost:8081}")
    private String userServiceUrl;

    /**
     * Create user profile in user-service after successful registration
     */
    public void createUserProfile(String name, String email, String phone) {
        try {
            CreateUserRequest request = new CreateUserRequest(name, email, phone);
            
            webClientBuilder.build()
                    .post()
                    .uri(userServiceUrl + "/api/internal/users")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnSuccess(response -> log.info("Successfully created user profile for email: {}", email))
                    .doOnError(error -> log.error("Failed to create user profile for email: {}. Error: {}", email, error.getMessage()))
                    .onErrorResume(error -> {
                        log.warn("User profile creation failed, but authentication was successful for email: {}", email);
                        return Mono.empty();
                    })
                    .subscribe(); // Non-blocking call
                    
        } catch (Exception e) {
            log.error("Error creating user profile for email: {}. Error: {}", email, e.getMessage());
            // Don't fail the authentication if user-service is down
        }
    }

    /**
     * DTO for creating user in user-service
     */
    public static class CreateUserRequest {
        private String name;
        private String email;
        private String phone;

        public CreateUserRequest(String name, String email, String phone) {
            this.name = name;
            this.email = email;
            this.phone = phone;
        }

        // Getters
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
    }
} 