package com.liftnet.liftnet_backend.postulante.dto;

public class PostulanteProfileResponse {

    private String nombre;
    private String apellidos;
    private String ubicacion;
    private String telefono;
    private String bio;
    private boolean disponible;

    public PostulanteProfileResponse(
            String nombre,
            String apellidos,
            String ubicacion,
            String telefono,
            String bio,
            boolean disponible) {

        this.nombre = nombre;
        this.apellidos = apellidos;
        this.ubicacion = ubicacion;
        this.telefono = telefono;
        this.bio = bio;
        this.disponible = disponible;
    }

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

    public boolean isDisponible() {
        return disponible;
    }
}