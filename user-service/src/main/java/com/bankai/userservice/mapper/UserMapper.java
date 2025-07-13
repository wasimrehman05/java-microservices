package com.bankai.userservice.mapper;

import com.bankai.userservice.dto.PagedResponse;
import com.bankai.userservice.dto.UserRequest;
import com.bankai.userservice.dto.UserResponse;
import com.bankai.userservice.entity.User;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserResponse toUserResponse(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        return dto;
    }

    public static User toUser(UserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        return user;
    }

    public static PagedResponse<UserResponse> toPagedResponse(Page<User> usersPage) {
        PagedResponse<UserResponse> pagedResponse = new PagedResponse<>();

        pagedResponse.setContent(
                usersPage.getContent()
                        .stream()
                        .map(UserMapper::toUserResponse)
                        .collect(Collectors.toList())
        );
        pagedResponse.setPageNumber(usersPage.getNumber());
        pagedResponse.setPageSize(usersPage.getSize());
        pagedResponse.setTotalElements(usersPage.getTotalElements());
        pagedResponse.setTotalPages(usersPage.getTotalPages());
        pagedResponse.setLast(usersPage.isLast());

        return pagedResponse;
    }
}
