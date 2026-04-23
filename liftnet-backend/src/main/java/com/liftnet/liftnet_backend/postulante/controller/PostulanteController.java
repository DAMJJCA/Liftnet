package com.liftnet.liftnet_backend.postulante.controller;

import com.liftnet.liftnet_backend.postulante.dto.PostulanteProfileRequest;
import com.liftnet.liftnet_backend.postulante.dto.PostulanteProfileResponse;
import com.liftnet.liftnet_backend.postulante.entity.PostulanteProfile;
import com.liftnet.liftnet_backend.postulante.service.PostulanteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/postulante/profile")
public class PostulanteController {

    private final PostulanteService service;

    public PostulanteController(PostulanteService service) {
        this.service = service;
    }

    /**
     * ==========================
     * ✅ MODO DESARROLLO (ACTIVO)
     * ==========================
     * - Sin JWT
     * - Sin roles
     * - email por request param
     */

    // VER PERFIL
    @GetMapping
    public PostulanteProfileResponse getMyProfile(
            @RequestParam String email) {

        PostulanteProfile profile = service.getMyProfile(email);
        return mapToResponse(profile);
    }

    // CREAR PERFIL
    @PostMapping
    public PostulanteProfileResponse createProfile(
            @RequestParam String email,
            @Valid @RequestBody PostulanteProfileRequest request) {

        PostulanteProfile profile =
                service.createProfile(email, request);
        return mapToResponse(profile);
    }

    // ACTUALIZAR PERFIL
    @PutMapping
    public PostulanteProfileResponse updateProfile(
            @RequestParam String email,
            @Valid @RequestBody PostulanteProfileRequest request) {

        PostulanteProfile profile =
                service.updateProfile(email, request);
        return mapToResponse(profile);
    }

    // CAMBIAR DISPONIBILIDAD
    @PatchMapping("/disponibilidad")
    public void updateDisponibilidad(
            @RequestParam String email,
            @RequestParam boolean disponible) {

        service.updateDisponibilidad(email, disponible);
    }

    /*
    ======================================================
    🔐 MODO PRODUCCIÓN (DESCOMENTAR CUANDO ACTIVE JWT)
    ======================================================

    @PreAuthorize("hasRole('POSTULANTE')")
    @GetMapping
    public PostulanteProfileResponse getMyProfile(Authentication authentication) {
        return mapToResponse(
                service.getMyProfile(authentication.getName())
        );
    }

    @PreAuthorize("hasRole('POSTULANTE')")
    @PostMapping
    public PostulanteProfileResponse createProfile(
            Authentication authentication,
            @Valid @RequestBody PostulanteProfileRequest request) {

        return mapToResponse(
                service.createProfile(authentication.getName(), request)
        );
    }

    @PreAuthorize("hasRole('POSTULANTE')")
    @PutMapping
    public PostulanteProfileResponse updateProfile(
            Authentication authentication,
            @Valid @RequestBody PostulanteProfileRequest request) {

        return mapToResponse(
                service.updateProfile(authentication.getName(), request)
        );
    }

    @PreAuthorize("hasRole('POSTULANTE')")
    @PatchMapping("/disponibilidad")
    public void updateDisponibilidad(
            Authentication authentication,
            @RequestParam boolean disponible) {

        service.updateDisponibilidad(authentication.getName(), disponible);
    }
    */

    // ==========================
    // MAPPER
    // ==========================

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