package com.liftnet.liftnet_backend.oferta.controller;

import com.liftnet.liftnet_backend.oferta.dto.OfertaRequest;
import com.liftnet.liftnet_backend.oferta.dto.OfertaResponse;
import com.liftnet.liftnet_backend.oferta.entity.OfertaTrabajo;
import com.liftnet.liftnet_backend.oferta.service.OfertaService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ofertas")
public class OfertaController {

    private final OfertaService service;

    public OfertaController(OfertaService service) {
        this.service = service;
    }

    // ºPOSTULANTE VE OFERTAS ACTIVAS (CON FILTRO OPCIONAL)
    @PreAuthorize("hasRole('POSTULANTE')")
    @GetMapping
    public List<OfertaResponse> getOfertasActivas(
            @RequestParam(required = false) String ubicacion) {

        return service.getOfertasActivas(ubicacion)
                .stream()
                .map(this::map)
                .toList();
    }

    // EMPRESA CREA OFERTA
    @PreAuthorize("hasRole('EMPRESA')")
    @PostMapping
    public OfertaResponse crearOferta(
            Authentication auth,
            @Valid @RequestBody OfertaRequest request) {

        return map(service.crearOferta(auth.getName(), request));
    }

    // EMPRESA VE SUS OFERTAS
    @PreAuthorize("hasRole('EMPRESA')")
    @GetMapping("/mis-ofertas")
    public List<OfertaResponse> getMisOfertas(Authentication auth) {
        return service.getMisOfertas(auth.getName())
                .stream()
                .map(this::map)
                .toList();
    }

    // EMPRESA CIERRA OFERTA
    @PreAuthorize("hasRole('EMPRESA')")
    @PutMapping("/{ofertaId}/cerrar")
    public void cerrarOferta(
            Authentication auth,
            @PathVariable UUID ofertaId) {

        service.cerrarOferta(auth.getName(), ofertaId);
    }

    private OfertaResponse map(OfertaTrabajo oferta) {
        return new OfertaResponse(
                oferta.getTitulo(),
                oferta.getDescripcion(),
                oferta.getUbicacion(),
                oferta.isActiva(),
                oferta.getCreatedAt()
        );
    }
}