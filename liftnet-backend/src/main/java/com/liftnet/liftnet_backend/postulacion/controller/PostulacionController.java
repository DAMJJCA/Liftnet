package com.liftnet.liftnet_backend.postulacion.controller;

import com.liftnet.liftnet_backend.common.response.ApiResponse;
import com.liftnet.liftnet_backend.postulacion.dto.PostulacionResponse;
import com.liftnet.liftnet_backend.postulacion.dto.PostulantePostulacionResponse;
import com.liftnet.liftnet_backend.postulacion.entity.EstadoPostulacion;
import com.liftnet.liftnet_backend.postulacion.entity.Postulacion;
import com.liftnet.liftnet_backend.postulacion.service.PostulacionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/postulaciones")
public class PostulacionController {

    private final PostulacionService service;

    public PostulacionController(PostulacionService service) {
        this.service = service;
    }

    @PostMapping("/oferta/{ofertaId}")
    public ApiResponse<Void> postular(
            @RequestParam String email,
            @PathVariable UUID ofertaId) {

        service.postular(email, ofertaId);
        return ApiResponse.ok("Postulación realizada con éxito", null);
    }

    @GetMapping("/mis-postulaciones")
    public ApiResponse<Page<PostulacionResponse>> getMisPostulaciones(
            @RequestParam String email,
            @PageableDefault(size = 20) Pageable pageable) {

        Page<PostulacionResponse> page = service
                .getMisPostulaciones(email, pageable)
                .map(this::mapPostulante);

        return ApiResponse.ok(page);
    }

    @GetMapping("/oferta/{ofertaId}")
    public ApiResponse<Page<PostulantePostulacionResponse>> getPostulacionesOferta(
            @RequestParam String email,
            @PathVariable UUID ofertaId,
            @PageableDefault(size = 20) Pageable pageable) {

        Page<PostulantePostulacionResponse> page = service.getPostulacionesOferta(email, ofertaId, pageable);
        return ApiResponse.ok(page);
    }

    @DeleteMapping("/{postulacionId}")
    public ApiResponse<Void> retirarPostulacion(
            @RequestParam String email,
            @PathVariable UUID postulacionId) {

        service.retirarPostulacion(email, postulacionId);
        return ApiResponse.ok("Candidatura retirada con éxito", null);
    }

    @PutMapping("/{postulacionId}/estado")
    public ApiResponse<Void> actualizarEstado(
            @RequestParam String email,
            @PathVariable UUID postulacionId,
            @RequestParam EstadoPostulacion estado) {

        service.actualizarEstado(email, postulacionId, estado);
        return ApiResponse.ok("Estado de postulación actualizado", null);
    }

    // MAPPERS PRIVADOS
    private PostulacionResponse mapPostulante(Postulacion p) {
        String emailContacto = "No especificado";
        if (p.getOferta().getEmpresa().getUser() != null) {
            emailContacto = p.getOferta().getEmpresa().getUser().getEmail();
        }

        return new PostulacionResponse(
                p.getId(),
                p.getOferta().getId(),
                p.getOferta().getTitulo(),
                p.getEstado(),
                p.getCreatedAt(),
                p.getOferta().getEmpresa().getNombreEmpresa(),
                p.getOferta().getUbicacion(),
                p.getOferta().getDescripcion(),
                p.getOferta().getEmpresa().getTelefono(),
                emailContacto
        );
    }
}