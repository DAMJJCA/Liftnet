package com.liftnet.liftnet_backend.postulante.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PostulanteProfileRequest {

    @NotBlank
    @Size(max = 100)
    private String nombre;

    @NotBlank
    @Size(max = 150)
    private String apellidos;

    @Size(max = 150)
    private String ubicacion;

    @Size(max = 20)
    private String telefono;

    @Size(max = 500)
    private String bio;

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getBio() {
        return bio;
    }
}