package com.liftnet.liftnet_backend.empresa.dto;

public class EmpresaProfileResponse {

    private String nombreEmpresa;
    private String ubicacion;
    private String telefono;
    private String descripcion;
    private String fotoUrl;

    public EmpresaProfileResponse(String nombreEmpresa, String ubicacion, String telefono, String descripcion, String fotoUrl) {
        this.nombreEmpresa = nombreEmpresa;
        this.ubicacion = ubicacion;
        this.telefono = telefono;
        this.descripcion = descripcion;
        this.fotoUrl = fotoUrl;
    }

	public String getNombreEmpresa() {
		return nombreEmpresa;
	}

	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFotoUrl() {
		return fotoUrl;
	}

	public void setFotoUrl(String fotoUrl) {
		this.fotoUrl = fotoUrl;
	}
}