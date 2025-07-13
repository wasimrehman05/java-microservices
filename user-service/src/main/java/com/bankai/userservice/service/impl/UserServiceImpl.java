package com.bankai.userservice.service.impl;

import com.bankai.userservice.dto.PagedResponse;
import com.bankai.userservice.dto.UserRequest;
import com.bankai.userservice.dto.UserResponse;
import com.bankai.userservice.entity.User;
import com.bankai.userservice.exception.UserNotFoundException;
import com.bankai.userservice.mapper.UserMapper;
import com.bankai.userservice.repository.UserRepository;
import com.bankai.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponse createUser(User user) {
        return UserMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return UserMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        return UserMapper.toUserResponse(user);
    }

    @Override
    public PagedResponse<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        return UserMapper.toPagedResponse(usersPage);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        existingUser.setName(userRequest.getName());
        existingUser.setEmail(userRequest.getEmail());
        existingUser.setPhone(userRequest.getPhone());

        User savedUser = userRepository.save(existingUser);

        return UserMapper.toUserResponse(savedUser);
    }

    @Override
    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException((id)));

        userRepository.delete(user);
    }
}
