package com.liftnet.liftnet_backend.user.controller;

import com.liftnet.liftnet_backend.common.mapper.UserMapper;
import com.liftnet.liftnet_backend.common.response.ApiResponse;
import com.liftnet.liftnet_backend.user.dto.UserResponse;
import com.liftnet.liftnet_backend.user.entity.Role;
import com.liftnet.liftnet_backend.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * ==========================
     * ✅ MODO DESARROLLO (ACTIVO)
     * ==========================
     * - Sin JWT
     * - Sin roles
     * - Endpoints abiertos para pruebas
     */

    // VER TODOS LOS USUARIOS (DEV)
    @GetMapping("/usuarios")
    public ApiResponse<Page<UserResponse>> getAllUsers(Pageable pageable) {

        Page<UserResponse> page = userService.getAllUsers(pageable)
                .map(UserMapper::toResponse);

        return ApiResponse.ok(page);
    }

    // CAMBIAR ROL DE USUARIO (DEV)
    @PutMapping("/usuarios/{id}/rol")
    public ApiResponse<Void> changeRole(
            @PathVariable UUID id,
            @RequestParam Role role) {

        userService.changeRole(id, role);
        return ApiResponse.ok("Rol actualizado correctamente", null);
    }

    // BLOQUEAR / DESBLOQUEAR USUARIO (DEV)
    @PutMapping("/usuarios/{id}/enabled")
    public ApiResponse<Void> setEnabled(
            @PathVariable UUID id,
            @RequestParam boolean enabled) {

        userService.setEnabled(id, enabled);

        String message = enabled
                ? "Usuario habilitado correctamente"
                : "Usuario deshabilitado correctamente";

        return ApiResponse.ok(message, null);
    }

    /*
    ======================================================
    🔐 MODO PRODUCCIÓN (DESCOMENTAR CUANDO ACTIVE JWT)
    ======================================================

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuarios")
    public ApiResponse<Page<UserResponse>> getAllUsers(Pageable pageable) {

        Page<UserResponse> page = userService.getAllUsers(pageable)
                .map(UserMapper::toResponse);

        return ApiResponse.ok(page);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/usuarios/{id}/rol")
    public ApiResponse<Void> changeRole(
            @PathVariable UUID id,
            @RequestParam Role role) {

        userService.changeRole(id, role);
        return ApiResponse.ok("Rol actualizado correctamente", null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/usuarios/{id}/enabled")
    public ApiResponse<Void> setEnabled(
            @PathVariable UUID id,
            @RequestParam boolean enabled) {

        userService.setEnabled(id, enabled);

        String message = enabled
                ? "Usuario habilitado correctamente"
                : "Usuario deshabilitado correctamente";

        return ApiResponse.ok(message, null);
    }
    */
}