package com.liftnet.liftnet_backend.common.mapper;

import com.liftnet.liftnet_backend.user.dto.UserResponse;
import com.liftnet.liftnet_backend.user.entity.User;

public class UserMapper {

    private UserMapper() {}

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.isEnabled(),
                user.getCreatedAt()
        );
    }
}