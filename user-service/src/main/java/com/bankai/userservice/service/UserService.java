package com.bankai.userservice.service;

import com.bankai.userservice.dto.PagedResponse;
import com.bankai.userservice.dto.UserRequest;
import com.bankai.userservice.dto.UserResponse;
import com.bankai.userservice.entity.User;
//import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse createUser(User user);

    UserResponse getUserById(Long id);

    UserResponse getUserByEmail(String email);

    PagedResponse<UserResponse> getAllUsers(Pageable pageable);

    UserResponse updateUser(Long id, UserRequest userRequest);

    void deleteUser(Long id);
}
