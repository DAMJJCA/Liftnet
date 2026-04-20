package com.liftnet.liftnet_backend.user.dto;

import com.liftnet.liftnet_backend.user.entity.Role;

import java.time.Instant;
import java.util.UUID;

public class UserResponse {

    private UUID id;
    private String email;
    private Role role;
    private boolean enabled;
    private Instant createdAt;

    public UserResponse(UUID id, String email, Role role, boolean enabled, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}