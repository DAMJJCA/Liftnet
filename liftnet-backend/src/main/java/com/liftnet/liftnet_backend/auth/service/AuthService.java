package com.liftnet.liftnet_backend.auth.service;

import com.liftnet.liftnet_backend.auth.dto.AuthRequest;
import com.liftnet.liftnet_backend.auth.dto.AuthResponse;
import com.liftnet.liftnet_backend.auth.dto.RegisterRequest;
import com.liftnet.liftnet_backend.common.exception.EmailAlreadyExistsException;
import com.liftnet.liftnet_backend.common.exception.InvalidCredentialsException;
import com.liftnet.liftnet_backend.config.security.JwtService;
import com.liftnet.liftnet_backend.user.entity.Role;
import com.liftnet.liftnet_backend.user.entity.User;
import com.liftnet.liftnet_backend.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {

        // VALIDACIÓN DE NEGOCIO
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.POSTULANTE);

        userRepository.save(user);

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthResponse(token, user.getRole().name());
    }

    public AuthResponse login(AuthRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthResponse(token, user.getRole().name());
    }
}
