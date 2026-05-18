package com.liftnet.liftnet_backend.empresa.controller;

import com.liftnet.liftnet_backend.empresa.dto.EmpresaProfileRequest;
import com.liftnet.liftnet_backend.empresa.dto.EmpresaProfileResponse;
import com.liftnet.liftnet_backend.empresa.entity.EmpresaProfile;
import com.liftnet.liftnet_backend.empresa.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/empresa/profile")
public class EmpresaController {

    private final EmpresaService service;

    public EmpresaController(EmpresaService service) {
        this.service = service;
    }

    /**
     * Obtiene el perfil de la empresa autenticada.
     * La identidad se resuelve internamente en el service mediante el SecurityContext.
     */
    @GetMapping
    @PreAuthorize("hasRole('EMPRESA')")
    public ResponseEntity<EmpresaProfileResponse> getMyProfile() {
        EmpresaProfile profile = service.getMyProfile();
        return ResponseEntity.ok(mapToResponse(profile));
    }

    /**
     * Crea el perfil corporativo para el usuario actual.
     */
    @PostMapping
    @PreAuthorize("hasRole('EMPRESA')")
    public ResponseEntity<EmpresaProfileResponse> createProfile(@Valid @RequestBody EmpresaProfileRequest request) {
        EmpresaProfile profile = service.createProfile(request);
        return ResponseEntity.ok(mapToResponse(profile));
    }

    /**
     * Actualiza los datos del perfil corporativo.
     */
    @PutMapping
    @PreAuthorize("hasRole('EMPRESA')")
    public ResponseEntity<EmpresaProfileResponse> updateProfile(@Valid @RequestBody EmpresaProfileRequest request) {
        EmpresaProfile profile = service.updateProfile(request);
        return ResponseEntity.ok(mapToResponse(profile));
    }

    /**
     * Mapea la entidad al DTO de respuesta.
     */
    private EmpresaProfileResponse mapToResponse(EmpresaProfile profile) {
        return new EmpresaProfileResponse(
                profile.getNombreEmpresa(),
                profile.getUbicacion(),
                profile.getTelefono(),
                profile.getDescripcion(),
                profile.getFotoUrl()
        );
    }
}