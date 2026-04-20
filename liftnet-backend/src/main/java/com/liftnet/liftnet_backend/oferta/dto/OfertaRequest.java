package com.liftnet.liftnet_backend.oferta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class OfertaRequest {

    @NotBlank
    @Size(max = 150)
    private String titulo;

    @Size(max = 500)
    private String descripcion;

    @Size(max = 150)
    private String ubicacion;

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }
}