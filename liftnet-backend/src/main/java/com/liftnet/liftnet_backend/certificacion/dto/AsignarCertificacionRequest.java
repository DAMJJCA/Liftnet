package com.liftnet.liftnet_backend.certificacion.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public class AsignarCertificacionRequest {

    @NotNull
    private UUID certificacionId;

    private LocalDate fechaObtencion;
    private LocalDate fechaExpiracion;

    public UUID getCertificacionId() {
        return certificacionId;
    }

    public void setCertificacionId(UUID certificacionId) {
        this.certificacionId = certificacionId;
    }

    public LocalDate getFechaObtencion() {
        return fechaObtencion;
    }

    public void setFechaObtencion(LocalDate fechaObtencion) {
        this.fechaObtencion = fechaObtencion;
    }

    public LocalDate getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDate fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }
}
