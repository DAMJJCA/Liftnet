package com.liftnet.liftnet_backend.experiencia.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class ExperienciaRequest {

    @NotBlank
    private String lugar;

    private String descripcion;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String archivoUrl;
    
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
	public String getArchivoUrl() {
		return archivoUrl;
	}
	public void setArchivoUrl(String archivoUrl) {
		this.archivoUrl = archivoUrl;
	}
}