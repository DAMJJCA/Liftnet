package com.liftnet.liftnet_backend.empresa.dto;

public class EmpresaProfileResponse {

    private String nombreEmpresa;
    private String ubicacion;
    private String telefono;
    private String descripcion;

    public EmpresaProfileResponse(
            String nombreEmpresa,
            String ubicacion,
            String telefono,
            String descripcion) {

        this.nombreEmpresa = nombreEmpresa;
        this.ubicacion = ubicacion;
        this.telefono = telefono;
        this.descripcion = descripcion;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDescripcion() {
        return descripcion;
    }
}