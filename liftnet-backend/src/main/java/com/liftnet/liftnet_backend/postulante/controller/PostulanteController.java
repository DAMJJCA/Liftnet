package com.liftnet.liftnet_backend.postulante.controller;

import com.liftnet.liftnet_backend.postulante.dto.PostulanteProfileRequest;
import com.liftnet.liftnet_backend.postulante.dto.PostulanteProfileResponse;
import com.liftnet.liftnet_backend.postulante.entity.PostulanteProfile;
import com.liftnet.liftnet_backend.postulante.service.PostulanteService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/postulante/profile")
public class PostulanteController {

    private final PostulanteService service;

    public PostulanteController(PostulanteService service) {
        this.service = service;
    }

    // VER MI PERFIL
    @PreAuthorize("hasRole('POSTULANTE')")
    @GetMapping
    public PostulanteProfileResponse getMyProfile(Authentication authentication) {
        PostulanteProfile profile =
                service.getMyProfile(authentication.getName());
        return mapToResponse(profile);
    }

    // CREAR PERFIL
    @PreAuthorize("hasRole('POSTULANTE')")
    @PostMapping
    public PostulanteProfileResponse createProfile(
            Authentication authentication,
            @Valid @RequestBody PostulanteProfileRequest request) {

        PostulanteProfile profile =
                service.createProfile(authentication.getName(), request);
        return mapToResponse(profile);
    }

    // ACTUALIZAR PERFIL
    @PreAuthorize("hasRole('POSTULANTE')")
    @PutMapping
    public PostulanteProfileResponse updateProfile(
            Authentication authentication,
            @Valid @RequestBody PostulanteProfileRequest request) {

        PostulanteProfile profile =
                service.updateProfile(authentication.getName(), request);
        return mapToResponse(profile);
    }

    // MARCAR DISPONIBILIDAD
    @PreAuthorize("hasRole('POSTULANTE')")
    @PatchMapping("/disponibilidad")
    public void updateDisponibilidad(
            Authentication authentication,
            @RequestParam boolean disponible) {

        service.updateDisponibilidad(authentication.getName(), disponible);
    }

    // MAPPER SIMPLE
    private PostulanteProfileResponse mapToResponse(PostulanteProfile profile) {
        return new PostulanteProfileResponse(
                profile.getNombre(),
                profile.getApellidos(),
                profile.getUbicacion(),
                profile.getTelefono(),
                profile.getBio(),
                profile.isDisponible()
        );
    }
}
