package com.liftnet.liftnet_backend.postulacion.dto;

import com.liftnet.liftnet_backend.postulacion.entity.EstadoPostulacion;

import java.time.Instant;
import java.util.UUID;

public class PostulantePostulacionResponse {

    private UUID postulacionId;
    private String nombrePostulante;
    private String apellidosPostulante;
    private EstadoPostulacion estado;
    private Instant createdAt;

    public PostulantePostulacionResponse(
            UUID postulacionId,
            String nombrePostulante,
            String apellidosPostulante,
            EstadoPostulacion estado,
            Instant createdAt) {

        this.postulacionId = postulacionId;
        this.nombrePostulante = nombrePostulante;
        this.apellidosPostulante = apellidosPostulante;
        this.estado = estado;
        this.createdAt = createdAt;
    }

    public UUID getPostulacionId() {
        return postulacionId;
    }

    public String getNombrePostulante() {
        return nombrePostulante;
    }

    public String getApellidosPostulante() {
        return apellidosPostulante;
    }

    public EstadoPostulacion getEstado() {
        return estado;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
