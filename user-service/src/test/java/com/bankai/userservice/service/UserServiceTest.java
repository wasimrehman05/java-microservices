package com.bankai.userservice.service;

import com.bankai.userservice.dto.UserRequest;
import com.bankai.userservice.dto.UserResponse;
import com.bankai.userservice.entity.User;
import com.bankai.userservice.mapper.UserMapper;
import com.bankai.userservice.repository.UserRepository;
import com.bankai.userservice.service.impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() {
        UserRequest request = new UserRequest("John Doe", "john@example.com", "1234567890");

        User userEntity = UserMapper.toUser(request);
        userEntity.setId(1L);

        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userEntity);

        UserResponse response = userServiceImpl.createUser(userEntity);

        assertNotNull(response);
        assertEquals("John Doe", response.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_DuplicateEmail() {
        UserRequest request = new UserRequest("Jane", "jane@example.com", "1234567890");
        User userEntity = UserMapper.toUser(request);

        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));

        assertThrows(RuntimeException.class, () -> userServiceImpl.createUser(userEntity));
    }
}
