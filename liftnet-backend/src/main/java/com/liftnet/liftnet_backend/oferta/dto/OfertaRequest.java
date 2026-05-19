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

    private String estudiosMinimos;
    private String experienciaMinima;
    private String idiomas;
    private String nivel;
    private Integer vacantes;
    private String salario;

    // GETTERS
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getUbicacion() { return ubicacion; }
    public String getEstudiosMinimos() { return estudiosMinimos; }
    public String getExperienciaMinima() { return experienciaMinima; }
    public String getIdiomas() { return idiomas; }
    public String getNivel() { return nivel; }
    public Integer getVacantes() { return vacantes; }
    public String getSalario() { return salario; }
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	public void setEstudiosMinimos(String estudiosMinimos) {
		this.estudiosMinimos = estudiosMinimos;
	}
	public void setExperienciaMinima(String experienciaMinima) {
		this.experienciaMinima = experienciaMinima;
	}
	public void setIdiomas(String idiomas) {
		this.idiomas = idiomas;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public void setVacantes(Integer vacantes) {
		this.vacantes = vacantes;
	}
	public void setSalario(String salario) {
		this.salario = salario;
	}
    
}