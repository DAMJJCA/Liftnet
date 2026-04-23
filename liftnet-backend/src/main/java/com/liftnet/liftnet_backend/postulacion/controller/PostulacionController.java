package com.liftnet.liftnet_backend.postulacion.controller;

import com.liftnet.liftnet_backend.common.response.ApiResponse;
import com.liftnet.liftnet_backend.postulacion.dto.PostulacionResponse;
import com.liftnet.liftnet_backend.postulacion.dto.PostulantePostulacionResponse;
import com.liftnet.liftnet_backend.postulacion.entity.EstadoPostulacion;
import com.liftnet.liftnet_backend.postulacion.entity.Postulacion;
import com.liftnet.liftnet_backend.postulacion.service.PostulacionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/postulaciones")
public class PostulacionController {

    private final PostulacionService service;

    public PostulacionController(PostulacionService service) {
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

    // POSTULANTE SE POSTULA A UNA OFERTA
    @PostMapping("/oferta/{ofertaId}")
    public ApiResponse<Void> postular(
            @RequestParam String email,
            @PathVariable UUID ofertaId) {

        service.postular(email, ofertaId);
        return ApiResponse.ok("Postulación realizada con éxito", null);
    }

    // POSTULANTE VE SUS POSTULACIONES
    @GetMapping("/mis-postulaciones")
    public ApiResponse<Page<PostulacionResponse>> getMisPostulaciones(
            @RequestParam String email,
            Pageable pageable) {

        Page<PostulacionResponse> page = service
                .getMisPostulaciones(email, pageable)
                .map(this::mapPostulante);

        return ApiResponse.ok(page);
    }

    // EMPRESA VE POSTULACIONES DE UNA OFERTA
    @GetMapping("/oferta/{ofertaId}")
    public ApiResponse<Page<PostulantePostulacionResponse>> getPostulacionesOferta(
            @RequestParam String email,
            @PathVariable UUID ofertaId,
            Pageable pageable) {

        Page<PostulantePostulacionResponse> page = service
                .getPostulacionesOferta(email, ofertaId, pageable)
                .map(this::mapEmpresa);

        return ApiResponse.ok(page);
    }

    // EMPRESA ACEPTA / RECHAZA POSTULACIÓN
    @PutMapping("/{postulacionId}/estado")
    public ApiResponse<Void> actualizarEstado(
            @RequestParam String email,
            @PathVariable UUID postulacionId,
            @RequestParam EstadoPostulacion estado) {

        service.actualizarEstado(email, postulacionId, estado);
        return ApiResponse.ok("Estado de postulación actualizado", null);
    }

    // ==========================
    // MAPPERS PRIVADOS
    // ==========================

    private PostulacionResponse mapPostulante(Postulacion p) {
        return new PostulacionResponse(
                p.getId(),
                p.getOferta().getId(),
                p.getOferta().getTitulo(),
                p.getEstado(),
                p.getCreatedAt()
        );
    }

    private PostulantePostulacionResponse mapEmpresa(Postulacion p) {
        return new PostulantePostulacionResponse(
                p.getId(),
                p.getPostulante().getNombre(),
                p.getPostulante().getApellidos(),
                p.getEstado(),
                p.getCreatedAt()
        );
    }

    /*
    ======================================================
    🔐 MODO PRODUCCIÓN (DESCOMENTAR CUANDO ACTIVE JWT)
    ======================================================

    @PreAuthorize("hasRole('POSTULANTE')")
    @PostMapping("/oferta/{ofertaId}")
    public ApiResponse<Void> postular(
            Authentication auth,
            @PathVariable UUID ofertaId) {

        service.postular(auth.getName(), ofertaId);
        return ApiResponse.ok("Postulación realizada con éxito", null);
    }

    @PreAuthorize("hasRole('POSTULANTE')")
    @GetMapping("/mis-postulaciones")
    public ApiResponse<Page<PostulacionResponse>> getMisPostulaciones(
            Authentication auth,
            Pageable pageable) {

        Page<PostulacionResponse> page = service
                .getMisPostulaciones(auth.getName(), pageable)
                .map(this::mapPostulante);

        return ApiResponse.ok(page);
    }

    @PreAuthorize("hasRole('EMPRESA')")
    @GetMapping("/oferta/{ofertaId}")
    public ApiResponse<Page<PostulantePostulacionResponse>> getPostulacionesOferta(
            Authentication auth,
            @PathVariable UUID ofertaId,
            Pageable pageable) {

        Page<PostulantePostulacionResponse> page = service
                .getPostulacionesOferta(auth.getName(), ofertaId, pageable)
                .map(this::mapEmpresa);

        return ApiResponse.ok(page);
    }

    @PreAuthorize("hasRole('EMPRESA')")
    @PutMapping("/{postulacionId}/estado")
    public ApiResponse<Void> actualizarEstado(
            Authentication auth,
            @PathVariable UUID postulacionId,
            @RequestParam EstadoPostulacion estado) {

        service.actualizarEstado(auth.getName(), postulacionId, estado);
        return ApiResponse.ok("Estado de postulación actualizado", null);
    }
    */
}
