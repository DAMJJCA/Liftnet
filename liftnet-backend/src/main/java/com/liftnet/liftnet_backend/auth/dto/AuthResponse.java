package com.liftnet.liftnet_backend.auth.dto;

public class AuthResponse {

    private String accessToken;
    private String role;
    private boolean profileCompleted;

    public AuthResponse(String accessToken, String role,boolean profileCompleted) {
        this.accessToken = accessToken;
        this.role = role;
        this.profileCompleted = profileCompleted;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRole() {
        return role;
    }

    public boolean isProfileCompleted() {
        return profileCompleted;
    }
}