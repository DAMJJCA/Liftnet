package com.liftnet.liftnet_backend.experiencia.controller;

import com.liftnet.liftnet_backend.common.response.ApiResponse;
import com.liftnet.liftnet_backend.experiencia.dto.ExperienciaRequest;
import com.liftnet.liftnet_backend.experiencia.dto.ExperienciaResponse;
import com.liftnet.liftnet_backend.experiencia.service.ExperienciaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/experiencias")
public class ExperienciaController {

    private final ExperienciaService service;

    public ExperienciaController(ExperienciaService service) {
        this.service = service;
    }

    /**
     * ==========================
     * ✅ MODO DESARROLLO (ACTIVO)
     * ==========================
     * Se pasa el email por request param
     */

    // LISTAR EXPERIENCIAS DEL POSTULANTE
    @GetMapping("/mis-experiencias")
    public ApiResponse<Page<ExperienciaResponse>> listar(
            @RequestParam String email,
            Pageable pageable) {

        return ApiResponse.ok(
                service.listar(email, pageable)
                        .map(e -> new ExperienciaResponse(
                                e.getId(),
                                e.getLugar(),
                                e.getDescripcion(),
                                e.getFechaInicio(),
                                e.getFechaFin()
                        ))
        );
    }

    // CREAR EXPERIENCIA
    @PostMapping
    public ApiResponse<Void> crear(
            @RequestParam String email,
            @Valid @RequestBody ExperienciaRequest request) {

        service.crear(email, request);
        return ApiResponse.ok("Experiencia creada correctamente", null);
    }

    // ACTUALIZAR EXPERIENCIA
    @PutMapping("/{id}")
    public ApiResponse<Void> actualizar(
            @RequestParam String email,
            @PathVariable UUID id,
            @Valid @RequestBody ExperienciaRequest request) {

        service.actualizar(email, id, request);
        return ApiResponse.ok("Experiencia actualizada correctamente", null);
    }

    // ELIMINAR EXPERIENCIA
    @DeleteMapping("/{id}")
    public ApiResponse<Void> eliminar(
            @RequestParam String email,
            @PathVariable UUID id) {

        service.eliminar(email, id);
        return ApiResponse.ok("Experiencia eliminada correctamente", null);
    }

    /*
    ======================================================
    🔐 MODO PRODUCCIÓN (DESCOMENTAR CUANDO ACTIVE JWT)
    ======================================================

    @PreAuthorize("hasRole('POSTULANTE')")
    @GetMapping("/mis-experiencias")
    public ApiResponse<Page<ExperienciaResponse>> listar(
            Authentication auth,
            Pageable pageable) {

        return ApiResponse.ok(
                service.listar(auth.getName(), pageable)
                        .map(e -> new ExperienciaResponse(
                                e.getId(),
                                e.getLugar(),
                                e.getDescripcion(),
                                e.getFechaInicio(),
                                e.getFechaFin()
                        ))
        );
    }

    @PreAuthorize("hasRole('POSTULANTE')")
    @PostMapping
    public ApiResponse<Void> crear(
            Authentication auth,
            @Valid @RequestBody ExperienciaRequest request) {

        service.crear(auth.getName(), request);
        return ApiResponse.ok("Experiencia creada correctamente", null);
    }

    @PreAuthorize("hasRole('POSTULANTE')")
    @PutMapping("/{id}")
    public ApiResponse<Void> actualizar(
            Authentication auth,
            @PathVariable UUID id,
            @Valid @RequestBody ExperienciaRequest request) {

        service.actualizar(auth.getName(), id, request);
        return ApiResponse.ok("Experiencia actualizada correctamente", null);
    }

    @PreAuthorize("hasRole('POSTULANTE')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> eliminar(
            Authentication auth,
            @PathVariable UUID id) {

        service.eliminar(auth.getName(), id);
        return ApiResponse.ok("Experiencia eliminada correctamente", null);
    }
    */
}
