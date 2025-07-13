package com.bankai.userservice.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // For user-service, we don't need to validate users against database
        // since we only validate JWT tokens. The JWT already contains user info.
        // This is a simple implementation that allows any user with a valid JWT.
        return User.builder()
                .username(email)
                .password("") // Password not needed for JWT validation
                .roles("USER") // Default role
                .build();
    }
} 