package com.liftnet.liftnet_backend.auth.controller;

import com.liftnet.liftnet_backend.auth.dto.AuthRequest;
import com.liftnet.liftnet_backend.auth.dto.AuthResponse;
import com.liftnet.liftnet_backend.auth.dto.RegisterRequest;
import com.liftnet.liftnet_backend.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}