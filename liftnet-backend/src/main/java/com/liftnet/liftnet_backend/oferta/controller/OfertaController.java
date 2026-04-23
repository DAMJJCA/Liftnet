package com.liftnet.liftnet_backend.oferta.controller;

import com.liftnet.liftnet_backend.common.mapper.OfertaMapper;
import com.liftnet.liftnet_backend.common.response.ApiResponse;
import com.liftnet.liftnet_backend.oferta.dto.OfertaRequest;
import com.liftnet.liftnet_backend.oferta.dto.OfertaResponse;
import com.liftnet.liftnet_backend.oferta.service.OfertaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ofertas")
public class OfertaController {

    private final OfertaService service;

    public OfertaController(OfertaService service) {
        this.service = service;
    }

    /**
     * ==========================
     * ✅ MODO DESARROLLO (ACTIVO)
     * ==========================
     * - Sin JWT
     * - Sin roles
     * - email por request param solo cuando es necesario
     */

    // POSTULANTE VE OFERTAS ACTIVAS
    @GetMapping
    public ApiResponse<Page<OfertaResponse>> getOfertasActivas(
            @RequestParam(required = false) String ubicacion,
            Pageable pageable) {

        return ApiResponse.ok(
                service.getOfertasActivas(ubicacion, pageable)
                        .map(OfertaMapper::toResponse)
        );
    }

    // EMPRESA CREA OFERTA
    @PostMapping
    public ApiResponse<OfertaResponse> crearOferta(
            @RequestParam String email,
            @Valid @RequestBody OfertaRequest request) {

        return ApiResponse.ok(
                "Oferta creada correctamente",
                OfertaMapper.toResponse(
                        service.crearOferta(email, request)
                )
        );
    }

    // EMPRESA VE SUS OFERTAS
    @GetMapping("/mis-ofertas")
    public ApiResponse<Page<OfertaResponse>> getMisOfertas(
            @RequestParam String email,
            Pageable pageable) {

        return ApiResponse.ok(
                service.getMisOfertas(email, pageable)
                        .map(OfertaMapper::toResponse)
        );
    }

    // EMPRESA CIERRA OFERTA
    @PutMapping("/{ofertaId}/cerrar")
    public ApiResponse<Void> cerrarOferta(
            @RequestParam String email,
            @PathVariable UUID ofertaId) {

        service.cerrarOferta(email, ofertaId);
        return ApiResponse.ok("Oferta cerrada", null);
    }

    /*
    ======================================================
    🔐 MODO PRODUCCIÓN (DESCOMENTAR CUANDO ACTIVE JWT)
    ======================================================

    // POSTULANTE VE OFERTAS
    @PreAuthorize("hasRole('POSTULANTE')")
    @GetMapping
    public ApiResponse<Page<OfertaResponse>> getOfertasActivas(
            @RequestParam(required = false) String ubicacion,
            Pageable pageable) {

        return ApiResponse.ok(
                service.getOfertasActivas(ubicacion, pageable)
                        .map(OfertaMapper::toResponse)
        );
    }

    // EMPRESA CREA OFERTA
    @PreAuthorize("hasRole('EMPRESA')")
    @PostMapping
    public ApiResponse<OfertaResponse> crearOferta(
            Authentication auth,
            @Valid @RequestBody OfertaRequest request) {

        return ApiResponse.ok(
                "Oferta creada correctamente",
                OfertaMapper.toResponse(
                        service.crearOferta(auth.getName(), request)
                )
        );
    }

    // EMPRESA VE SUS OFERTAS
    @PreAuthorize("hasRole('EMPRESA')")
    @GetMapping("/mis-ofertas")
    public ApiResponse<Page<OfertaResponse>> getMisOfertas(
            Authentication auth,
            Pageable pageable) {

        return ApiResponse.ok(
                service.getMisOfertas(auth.getName(), pageable)
                        .map(OfertaMapper::toResponse)
        );
    }

    // EMPRESA CIERRA OFERTA
    @PreAuthorize("hasRole('EMPRESA')")
    @PutMapping("/{ofertaId}/cerrar")
    public ApiResponse<Void> cerrarOferta(
            Authentication auth,
            @PathVariable UUID ofertaId) {

        service.cerrarOferta(auth.getName(), ofertaId);
        return ApiResponse.ok("Oferta cerrada", null);
    }
    */
}