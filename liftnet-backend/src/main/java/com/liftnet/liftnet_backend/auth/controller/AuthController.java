package com.liftnet.liftnet_backend.auth.controller;

import com.liftnet.liftnet_backend.auth.dto.AuthRequest;
import com.liftnet.liftnet_backend.auth.dto.AuthResponse;
import com.liftnet.liftnet_backend.auth.dto.RegisterRequest;
import com.liftnet.liftnet_backend.auth.service.AuthService;
import com.liftnet.liftnet_backend.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * ==========================
     * ✅ MODO DESARROLLO (ACTIVO)
     * ==========================
     * - Endpoints públicos
     * - No requieren JWT
     * - Funcionan tanto en DEV como en PROD
     */

    // LOGIN
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(
            @Valid @RequestBody AuthRequest request) {

        AuthResponse response = authService.login(request);

        return ApiResponse.ok(
                "Login realizado con éxito",
                response
        );
    }

    // REGISTER
    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        System.out.println(">>> ENTRÓ A AuthController.register <<<");

        AuthResponse response = authService.register(request);

        return ApiResponse.ok(
                "Usuario registrado correctamente",
                response
        );
    }

    /*
    ======================================================
    🔐 NOTA SOBRE PRODUCCIÓN
    ======================================================

    - Estos endpoints DEBEN ser públicos también en producción
    - En SecurityConfig PROD ya tienes:
        .requestMatchers("/api/v1/auth/**").permitAll()

    👉 No se añade @PreAuthorize aquí a propósito.
    👉 Si en el futuro quieres:
        - Registro solo ADMIN
        - Login externo (OAuth, Supabase Auth, etc.)

    Este será el sitio correcto para cambiarlo.
    */
}