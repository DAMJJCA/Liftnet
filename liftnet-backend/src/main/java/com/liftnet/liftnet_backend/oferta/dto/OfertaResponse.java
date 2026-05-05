package com.liftnet.liftnet_backend.oferta.dto;

import java.time.Instant;
import java.util.UUID;

public class OfertaResponse {

    private UUID id; 
    private String titulo;
    private String descripcion;
    private String ubicacion;
    private boolean activa;
    private Instant createdAt;

    public OfertaResponse(
            UUID id,
            String titulo,
            String descripcion,
            String ubicacion,
            boolean activa,
            Instant createdAt) {
        
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.activa = activa;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public boolean isActiva() {
        return activa;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}