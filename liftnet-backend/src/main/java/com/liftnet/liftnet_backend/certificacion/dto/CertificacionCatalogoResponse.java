package com.liftnet.liftnet_backend.certificacion.dto;

import java.util.UUID;

public class CertificacionCatalogoResponse {

    private UUID id;
    private String nombre;
    private String entidad;
    private String archivoUrl;

    public CertificacionCatalogoResponse(UUID id, String nombre, String entidad,String archivoUrl) {
        this.id = id;
        this.nombre = nombre;
        this.entidad = entidad;
        this.archivoUrl=archivoUrl;
        
    }

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEntidad() {
		return entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public String getArchivoUrl() {
		return archivoUrl;
	}

	public void setArchivoUrl(String archivoUrl) {
		this.archivoUrl = archivoUrl;
	}

}
