package com.bankai.authservice.service;

import com.bankai.authservice.dto.AuthResponse;
import com.bankai.authservice.dto.LoginRequest;
import com.bankai.authservice.dto.RegisterRequest;
import com.bankai.authservice.entity.User;
import com.bankai.authservice.repository.UserRepository;
import com.bankai.authservice.service.impl.AuthServiceImpl;
import com.bankai.securitycommon.util.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtUtil jwtUtil;
    @Mock UserServiceClient userServiceClient;

    @InjectMocks AuthServiceImpl authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User userEntity;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setName("Alice");
        registerRequest.setEmail("alice@example.com");
        registerRequest.setPassword("pass123");
        registerRequest.setPhone("+1234567890");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("alice@example.com");
        loginRequest.setPassword("pass123");

        userEntity = User.builder()
                .id(10L)
                .name("Alice")
                .email("alice@example.com")
                .password("encodedPass")
                .role("USER")
                .build();
    }

    @Test
    void registerSuccess() {
        // Given: email not taken
        when(userRepository.findByEmail(registerRequest.getEmail()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword()))
                .thenReturn("encodedPass");
        when(userRepository.save(any(User.class)))
                .thenReturn(userEntity);
        when(jwtUtil.generateToken(eq("alice@example.com"), any(Map.class)))
                .thenReturn("jwt-token");

        // When
        AuthResponse resp = authService.register(registerRequest);

        // Then
        assertNotNull(resp);
        assertEquals("jwt-token", resp.getToken());
        assertEquals("Bearer", resp.getType());
        assertEquals("alice@example.com", resp.getEmail());
        assertEquals("Alice", resp.getName());
        assertEquals("USER", resp.getRole());
        
        verify(userRepository).save(argThat(u ->
                u.getEmail().equals("alice@example.com") &&
                        u.getPassword().equals("encodedPass") &&
                        u.getRole().equals("USER")
        ));
        verify(userServiceClient).createUserProfile("Alice", "alice@example.com", "+1234567890");
    }

    @Test
    void registerDuplicateEmailThrows() {
        // Given: email already exists
        when(userRepository.findByEmail(registerRequest.getEmail()))
                .thenReturn(Optional.of(userEntity));

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.register(registerRequest));
        assertTrue(ex.getMessage().contains("already exists"));
        verify(userServiceClient, never()).createUserProfile(any(), any(), any());
    }

    @Test
    void loginSuccess() {
        // Given: user exists and password matches
        when(userRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword()))
                .thenReturn(true);
        when(jwtUtil.generateToken(eq("alice@example.com"), any(Map.class)))
                .thenReturn("jwt-token");

        // When
        AuthResponse resp = authService.login(loginRequest);

        // Then
        assertNotNull(resp);
        assertEquals("jwt-token", resp.getToken());
        assertEquals("Bearer", resp.getType());
        assertEquals("alice@example.com", resp.getEmail());
        assertEquals("Alice", resp.getName());
        assertEquals("USER", resp.getRole());
    }

    @Test
    void loginInvalidPasswordThrows() {
        when(userRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword()))
                .thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> authService.login(loginRequest));
    }

    @Test
    void loginUserNotFoundThrows() {
        when(userRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> authService.login(loginRequest));
    }
}
