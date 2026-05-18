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
    private String archivoUrl;

    public CertificacionPostulanteResponse(
            UUID id,
            UUID certificacionId,
            String nombre,
            String entidad,
            LocalDate fechaObtencion,
            LocalDate fechaExpiracion,
            String archivoUrl) {

        this.id = id;
        this.certificacionId = certificacionId;
        this.nombre = nombre;
        this.entidad = entidad;
        this.fechaObtencion = fechaObtencion;
        this.fechaExpiracion = fechaExpiracion;
        this.archivoUrl=archivoUrl;
    }

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getCertificacionId() {
		return certificacionId;
	}

	public void setCertificacionId(UUID certificacionId) {
		this.certificacionId = certificacionId;
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

	public String getArchivoUrl() {
		return archivoUrl;
	}

	public void setArchivoUrl(String archivoUrl) {
		this.archivoUrl = archivoUrl;
	}
}