package com.bankai.authservice.service;

import com.bankai.authservice.dto.AuthResponse;
import com.bankai.authservice.dto.LoginRequest;
import com.bankai.authservice.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
