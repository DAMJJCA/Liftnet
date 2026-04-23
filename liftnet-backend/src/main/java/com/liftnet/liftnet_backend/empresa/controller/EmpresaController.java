package com.liftnet.liftnet_backend.empresa.controller;

import com.liftnet.liftnet_backend.empresa.dto.EmpresaProfileRequest;
import com.liftnet.liftnet_backend.empresa.dto.EmpresaProfileResponse;
import com.liftnet.liftnet_backend.empresa.entity.EmpresaProfile;
import com.liftnet.liftnet_backend.empresa.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/empresa/profile")
public class EmpresaController {

    private final EmpresaService service;

    public EmpresaController(EmpresaService service) {
        this.service = service;
    }

    /**
     * ==========================
     * ✅ MODO DESARROLLO (ACTIVO)
     * ==========================
     * Se pasa el email por request param
     */

    @GetMapping
    public EmpresaProfileResponse getMyProfile(
            @RequestParam String email) {

        EmpresaProfile profile = service.getMyProfile(email);
        return map(profile);
    }

    @PostMapping
    public EmpresaProfileResponse createProfile(
            @RequestParam String email,
            @Valid @RequestBody EmpresaProfileRequest request) {

        EmpresaProfile profile = service.createProfile(email, request);
        return map(profile);
    }

    @PutMapping
    public EmpresaProfileResponse updateProfile(
            @RequestParam String email,
            @Valid @RequestBody EmpresaProfileRequest request) {

        EmpresaProfile profile = service.updateProfile(email, request);
        return map(profile);
    }

    /*
    ======================================================
    🔐 MODO PRODUCCIÓN (DESCOMENTAR)
    ======================================================

    @PreAuthorize("hasRole('EMPRESA')")
    @GetMapping
    public EmpresaProfileResponse getMyProfile(Authentication auth) {
        return map(service.getMyProfile(auth.getName()));
    }

    @PreAuthorize("hasRole('EMPRESA')")
    @PostMapping
    public EmpresaProfileResponse createProfile(
            Authentication auth,
            @Valid @RequestBody EmpresaProfileRequest request) {

        return map(service.createProfile(auth.getName(), request));
    }

    @PreAuthorize("hasRole('EMPRESA')")
    @PutMapping
    public EmpresaProfileResponse updateProfile(
            Authentication auth,
            @Valid @RequestBody EmpresaProfileRequest request) {

        return map(service.updateProfile(auth.getName(), request));
    }
    */

    private EmpresaProfileResponse map(EmpresaProfile profile) {
        return new EmpresaProfileResponse(
                profile.getNombreEmpresa(),
                profile.getUbicacion(),
                profile.getTelefono(),
                profile.getDescripcion()
        );
    }
}