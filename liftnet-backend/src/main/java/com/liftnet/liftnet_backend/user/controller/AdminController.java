package com.liftnet.liftnet_backend.user.controller;

import com.liftnet.liftnet_backend.common.mapper.UserMapper;
import com.liftnet.liftnet_backend.common.response.ApiResponse;
import com.liftnet.liftnet_backend.user.dto.UserResponse;
import com.liftnet.liftnet_backend.user.entity.Role;
import com.liftnet.liftnet_backend.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // ADMIN VE TODOS LOS USUARIOS (PAGINADO)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuarios")
    public ApiResponse<Page<UserResponse>> getAllUsers(Pageable pageable) {

        Page<UserResponse> page = userService.getAllUsers(pageable)
                .map(UserMapper::toResponse);

        return ApiResponse.ok(page);
    }

    // ADMIN CAMBIA ROL DE USUARIO
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/usuarios/{id}/rol")
    public ApiResponse<Void> changeRole(
            @PathVariable UUID id,
            @RequestParam Role role) {

        userService.changeRole(id, role);
        return ApiResponse.ok("Rol actualizado correctamente", null);
    }

    // ADMIN BLOQUEA / DESBLOQUEA USUARIO
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
}
