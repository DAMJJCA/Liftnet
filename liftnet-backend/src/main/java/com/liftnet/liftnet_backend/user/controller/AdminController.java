package com.liftnet.liftnet_backend.user.controller;

import com.liftnet.liftnet_backend.user.dto.UserResponse;
import com.liftnet.liftnet_backend.user.entity.Role;
import com.liftnet.liftnet_backend.user.entity.User;
import com.liftnet.liftnet_backend.user.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // ADMIN VE TODOS LOS USUARIOS
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuarios")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(this::map)
                .toList();
    }

    // ADMIN CAMBIA ROL
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/usuarios/{id}/rol")
    public void changeRole(
            @PathVariable UUID id,
            @RequestParam Role role) {

        userService.changeRole(id, role);
    }

    // ADMIN BLOQUEA / DESBLOQUEA USUARIO
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/usuarios/{id}/enabled")
    public void setEnabled(
            @PathVariable UUID id,
            @RequestParam boolean enabled) {

        userService.setEnabled(id, enabled);
    }

    private UserResponse map(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.isEnabled(),
                user.getCreatedAt()
        );
    }
}