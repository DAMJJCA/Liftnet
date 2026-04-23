package com.liftnet.liftnet_backend.certificacion.dto;

import java.time.LocalDate;
import java.util.UUID;

public class CertificacionPostulanteResponse {

    private UUID id; // ID postulante_certificacion
    private UUID certificacionId;
    private String nombre;
    private String entidad;
    private LocalDate fechaObtencion;
    private LocalDate fechaExpiracion;

    public CertificacionPostulanteResponse(
            UUID id,
            UUID certificacionId,
            String nombre,
            String entidad,
            LocalDate fechaObtencion,
            LocalDate fechaExpiracion) {

        this.id = id;
        this.certificacionId = certificacionId;
        this.nombre = nombre;
        this.entidad = entidad;
        this.fechaObtencion = fechaObtencion;
        this.fechaExpiracion = fechaExpiracion;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCertificacionId() {
        return certificacionId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEntidad() {
        return entidad;
    }

    public LocalDate getFechaObtencion() {
        return fechaObtencion;
    }

    public LocalDate getFechaExpiracion() {
        return fechaExpiracion;
    }
}