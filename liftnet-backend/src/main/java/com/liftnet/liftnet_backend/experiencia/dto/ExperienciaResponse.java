package com.liftnet.liftnet_backend.experiencia.dto;

import java.time.LocalDate;
import java.util.UUID;

public class ExperienciaResponse {

    private UUID id;
    private String lugar;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public ExperienciaResponse(
            UUID id,
            String lugar,
            String descripcion,
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        this.id = id;
        this.lugar = lugar;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}
}