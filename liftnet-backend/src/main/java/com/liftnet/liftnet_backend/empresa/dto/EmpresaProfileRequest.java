package com.liftnet.liftnet_backend.empresa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EmpresaProfileRequest {

    @NotBlank
    @Size(max = 150)
    private String nombreEmpresa;

    @Size(max = 150)
    private String ubicacion;

    @Size(max = 20)
    private String telefono;

    @Size(max = 500)
    private String descripcion;

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
