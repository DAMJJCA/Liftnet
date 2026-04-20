package com.liftnet.liftnet_backend.user.service;

import com.liftnet.liftnet_backend.common.exception.ResourceNotFoundException;
import com.liftnet.liftnet_backend.user.entity.Role;
import com.liftnet.liftnet_backend.user.entity.User;
import com.liftnet.liftnet_backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void changeRole(UUID userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        user.setRole(newRole);
        userRepository.save(user);
    }

    public void setEnabled(UUID userId, boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        user.setEnabled(enabled);
        userRepository.save(user);
    }
}