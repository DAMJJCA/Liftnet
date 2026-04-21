package com.liftnet.liftnet_backend.postulacion.dto;

import com.liftnet.liftnet_backend.postulacion.entity.EstadoPostulacion;

import java.time.Instant;
import java.util.UUID;

public class PostulacionResponse {

    private UUID id;
    private UUID ofertaId;
    private String tituloOferta;
    private EstadoPostulacion estado;
    private Instant createdAt;

    public PostulacionResponse(
            UUID id,
            UUID ofertaId,
            String tituloOferta,
            EstadoPostulacion estado,
            Instant createdAt) {

        this.id = id;
        this.ofertaId = ofertaId;
        this.tituloOferta = tituloOferta;
        this.estado = estado;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOfertaId() {
        return ofertaId;
    }

    public String getTituloOferta() {
        return tituloOferta;
    }

    public EstadoPostulacion getEstado() {
        return estado;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}