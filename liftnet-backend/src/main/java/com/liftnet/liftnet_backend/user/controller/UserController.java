package com.liftnet.liftnet_backend.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    /**
     * ==========================
     * ✅ MODO DESARROLLO (ACTIVO)
     * ==========================
     * Endpoint de prueba para comprobar que:
     * - La API responde
     * - El backend está levantado
     * - No hay bloqueo de seguridad
     */

    @GetMapping
    public String test() {
        return "API Liftnet funcionando (DEV)";
    }

    /*
    ======================================================
    🔐 MODO PRODUCCIÓN (OPCIONAL)
    ======================================================

    Si en producción quieres que este endpoint:
    - Requiera autenticación
    - Sirva como "whoami" o health check protegido

    Ejemplo:

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String test(Authentication authentication) {
        return "Usuario autenticado: " + authentication.getName();
    }
    */
}