package com.liftnet.liftnet_backend.auth.service;

import com.liftnet.liftnet_backend.auth.dto.*;
import com.liftnet.liftnet_backend.common.exception.EmailAlreadyExistsException;
import com.liftnet.liftnet_backend.common.exception.InvalidCredentialsException;
import com.liftnet.liftnet_backend.config.security.JwtService;
import com.liftnet.liftnet_backend.empresa.entity.EmpresaProfile;
import com.liftnet.liftnet_backend.empresa.repository.EmpresaRepository;
import com.liftnet.liftnet_backend.postulante.entity.PostulanteProfile;
import com.liftnet.liftnet_backend.postulante.repository.PostulanteRepository;
import com.liftnet.liftnet_backend.user.entity.Role;
import com.liftnet.liftnet_backend.user.entity.User;
import com.liftnet.liftnet_backend.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PostulanteRepository postulanteRepository;
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PostulanteRepository postulanteRepository,
            EmpresaRepository empresaRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.postulanteRepository = postulanteRepository;
        this.empresaRepository = empresaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // REGISTER
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        Role role = Role.valueOf(request.getRole());

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        userRepository.save(user);

        // Crear perfil inicial
        if (role == Role.POSTULANTE) {
            PostulanteProfile p = new PostulanteProfile();
            p.setUser(user);
            p.setNombre(request.getNombre());
            p.setApellidos(request.getApellidos());
            p.setBio(request.getBio());
            p.setUbicacion(request.getUbicacion());
            p.setTelefono(request.getTelefono());
            p.setDisponible(true);
            postulanteRepository.save(p);
        }

        if (role == Role.EMPRESA) {
            EmpresaProfile e = new EmpresaProfile();
            e.setUser(user);
            e.setNombreEmpresa(request.getNombreEmpresa());
            e.setDescripcion(request.getDescripcion());
            e.setUbicacion(request.getUbicacion());
            e.setTelefono(request.getTelefono());
            empresaRepository.save(e);
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        boolean profileCompleted = isProfileCompleted(user);

        return new AuthResponse(
                token,
                user.getRole().name(),
                profileCompleted
        );
    }

    // LOGIN
    public AuthResponse login(AuthRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        boolean profileCompleted = isProfileCompleted(user);

        return new AuthResponse(
                token,
                user.getRole().name(),
                profileCompleted
        );
    }

    // MÉTODO CENTRAL
    private boolean isProfileCompleted(User user) {

        if (user.getRole() == Role.EMPRESA) {
            return empresaRepository.findByUser(user)
                    .map(p -> p.getNombreEmpresa() != null && !p.getNombreEmpresa().isBlank())
                    .orElse(false);
        }

        if (user.getRole() == Role.POSTULANTE) {
            return postulanteRepository.findByUser(user)
                    .map(p -> p.getNombre() != null && !p.getNombre().isBlank())
                    .orElse(false);
        }

        return true; // ADMIN
    }
}
