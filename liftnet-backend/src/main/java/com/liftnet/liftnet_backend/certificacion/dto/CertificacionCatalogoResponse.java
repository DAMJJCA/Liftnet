package com.liftnet.liftnet_backend.certificacion.dto;

import java.util.UUID;

public class CertificacionCatalogoResponse {

    private UUID id;
    private String nombre;
    private String entidad;

    public CertificacionCatalogoResponse(UUID id, String nombre, String entidad) {
        this.id = id;
        this.nombre = nombre;
        this.entidad = entidad;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEntidad() {
        return entidad;
    }
}
