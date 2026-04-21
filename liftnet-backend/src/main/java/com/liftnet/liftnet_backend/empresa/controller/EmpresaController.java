package com.liftnet.liftnet_backend.empresa.controller;

import com.liftnet.liftnet_backend.empresa.dto.EmpresaProfileRequest;
import com.liftnet.liftnet_backend.empresa.dto.EmpresaProfileResponse;
import com.liftnet.liftnet_backend.empresa.entity.EmpresaProfile;
import com.liftnet.liftnet_backend.empresa.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/empresa/profile")
public class EmpresaController {

    private final EmpresaService service;

    public EmpresaController(EmpresaService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('EMPRESA')")
    @GetMapping
    public EmpresaProfileResponse getMyProfile(Authentication auth) {
        EmpresaProfile profile = service.getMyProfile(auth.getName());
        return map(profile);
    }

    @PreAuthorize("hasRole('EMPRESA')")
    @PostMapping
    public EmpresaProfileResponse createProfile(
            Authentication auth,
            @Valid @RequestBody EmpresaProfileRequest request) {

        EmpresaProfile profile = service.createProfile(auth.getName(), request);
        return map(profile);
    }

    @PreAuthorize("hasRole('EMPRESA')")
    @PutMapping
    public EmpresaProfileResponse updateProfile(
            Authentication auth,
            @Valid @RequestBody EmpresaProfileRequest request) {

        EmpresaProfile profile = service.updateProfile(auth.getName(), request);
        return map(profile);
    }

    private EmpresaProfileResponse map(EmpresaProfile profile) {
        return new EmpresaProfileResponse(
                profile.getNombreEmpresa(),
                profile.getUbicacion(),
                profile.getTelefono(),
                profile.getDescripcion()
        );
    }
}