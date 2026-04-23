package com.liftnet.liftnet_backend.certificacion.controller;

import com.liftnet.liftnet_backend.certificacion.dto.AsignarCertificacionRequest;
import com.liftnet.liftnet_backend.certificacion.dto.CertificacionPostulanteResponse;
import com.liftnet.liftnet_backend.certificacion.service.CertificacionService;
import com.liftnet.liftnet_backend.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/certificaciones")
public class CertificacionController {

    private final CertificacionService service;

    public CertificacionController(CertificacionService service) {
        this.service = service;
    }

    /**
     * ==========================
     * ✅ MODO DESARROLLO (ACTIVO)
     * ==========================
     * Se pasa el email por request param
     */

    // LISTAR CERTIFICACIONES DEL POSTULANTE
    @GetMapping("/mis-certificaciones")
    public ApiResponse<Page<CertificacionPostulanteResponse>> listar(
            @RequestParam String email,
            Pageable pageable) {

        return ApiResponse.ok(
                service.listarCertificacionesDePostulante(email, pageable)
                        .map(pc -> new CertificacionPostulanteResponse(
                                pc.getId(),
                                pc.getCertificacion().getId(),
                                pc.getCertificacion().getNombre(),
                                pc.getCertificacion().getEntidad(),
                                pc.getFechaObtencion(),
                                pc.getFechaExpiracion()
                        ))
        );
    }

    // ASIGNAR CERTIFICACIÓN AL POSTULANTE
    @PostMapping("/asignar")
    public ApiResponse<Void> asignar(
            @RequestParam String email,
            @Valid @RequestBody AsignarCertificacionRequest request) {

        service.asignarCertificacion(email, request);
        return ApiResponse.ok("Certificación asignada correctamente", null);
    }

    // ELIMINAR CERTIFICACIÓN DEL POSTULANTE
    @DeleteMapping("/{id}")
    public ApiResponse<Void> eliminar(
            @RequestParam String email,
            @PathVariable UUID id) {

        service.eliminarCertificacion(email, id);
        return ApiResponse.ok("Certificación eliminada correctamente", null);
    }

    /*
    ======================================================
    🔐 MODO PRODUCCIÓN (DESCOMENTAR CUANDO ACTIVE JWT)
    ======================================================

    @PreAuthorize("hasRole('POSTULANTE')")
    @GetMapping("/mis-certificaciones")
    public ApiResponse<Page<CertificacionPostulanteResponse>> listar(
            Authentication auth,
            Pageable pageable) {

        return ApiResponse.ok(
                service.listarCertificacionesDePostulante(auth.getName(), pageable)
                        .map(pc -> new CertificacionPostulanteResponse(
                                pc.getId(),
                                pc.getCertificacion().getId(),
                                pc.getCertificacion().getNombre(),
                                pc.getCertificacion().getEntidad(),
                                pc.getFechaObtencion(),
                                pc.getFechaExpiracion()
                        ))
        );
    }

    @PreAuthorize("hasRole('POSTULANTE')")
    @PostMapping("/asignar")
    public ApiResponse<Void> asignar(
            Authentication auth,
            @Valid @RequestBody AsignarCertificacionRequest request) {

        service.asignarCertificacion(auth.getName(), request);
        return ApiResponse.ok("Certificación asignada correctamente", null);
    }

    @PreAuthorize("hasRole('POSTULANTE')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> eliminar(
            Authentication auth,
            @PathVariable UUID id) {

        service.eliminarCertificacion(auth.getName(), id);
        return ApiResponse.ok("Certificación eliminada correctamente", null);
    }
    */
}
