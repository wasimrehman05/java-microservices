package com.bankai.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Health Check", description = "API for checking the health of the user service")
public class HealthCheckController {

    @GetMapping("/")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("User Service is up and running");
    }
}
