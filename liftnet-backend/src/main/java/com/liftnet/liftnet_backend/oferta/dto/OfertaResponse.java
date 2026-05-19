package com.liftnet.liftnet_backend.oferta.dto;

import java.time.Instant;
import java.util.UUID;

public class OfertaResponse {

    private UUID id; 
    private String titulo;
    private String descripcion;
    private String ubicacion;
    private boolean activa;
    private Instant createdAt;
    private String estudiosMinimos;
    private String experienciaMinima;
    private String idiomas;
    private String nivel;
    private Integer vacantes;
    private String salario;

    public OfertaResponse(
            UUID id,
            String titulo,
            String descripcion,
            String ubicacion,
            boolean activa,
            Instant createdAt,
            String estudiosMinimos,
            String experienciaMinima,
            String idiomas,
            String nivel,
            Integer vacantes,
            String salario) {
        
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.activa = activa;
        this.createdAt = createdAt;
        this.estudiosMinimos = estudiosMinimos;
        this.experienciaMinima = experienciaMinima;
        this.idiomas = idiomas;
        this.nivel = nivel;
        this.vacantes = vacantes;
        this.salario = salario;
    }

    // GETTERS
    public UUID getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getUbicacion() { return ubicacion; }
    public boolean isActiva() { return activa; }
    public Instant getCreatedAt() { return createdAt; }
    public String getEstudiosMinimos() { return estudiosMinimos; }
    public String getExperienciaMinima() { return experienciaMinima; }
    public String getIdiomas() { return idiomas; }
    public String getNivel() { return nivel; }
    public Integer getVacantes() { return vacantes; }
    public String getSalario() { return salario; }

	public void setId(UUID id) {
		this.id = id;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
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