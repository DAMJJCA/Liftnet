package com.liftnet.liftnet_backend.postulacion.dto;

import com.liftnet.liftnet_backend.postulacion.entity.EstadoPostulacion;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class PostulantePostulacionResponse {

    private UUID postulacionId;
    private String nombrePostulante;
    private String apellidosPostulante;
    private EstadoPostulacion estado;
    private Instant createdAt;
    private List<?> experiencias;
    private List<?> certificaciones;
    private String cvUrl;

    public PostulantePostulacionResponse(
            UUID postulacionId,
            String nombrePostulante,
            String apellidosPostulante,
            EstadoPostulacion estado,
            Instant createdAt,
            List<?> experiencias,
            List<?> certificaciones,
            String cvUrl) {

        this.postulacionId = postulacionId;
        this.nombrePostulante = nombrePostulante;
        this.apellidosPostulante = apellidosPostulante;
        this.estado = estado;
        this.createdAt = createdAt;
        this.experiencias = experiencias;
        this.certificaciones = certificaciones;
        this.cvUrl=cvUrl;
    }

	public UUID getPostulacionId() {
		return postulacionId;
	}

	public void setPostulacionId(UUID postulacionId) {
		this.postulacionId = postulacionId;
	}

	public String getNombrePostulante() {
		return nombrePostulante;
	}

	public void setNombrePostulante(String nombrePostulante) {
		this.nombrePostulante = nombrePostulante;
	}

	public String getApellidosPostulante() {
		return apellidosPostulante;
	}

	public void setApellidosPostulante(String apellidosPostulante) {
		this.apellidosPostulante = apellidosPostulante;
	}

	public EstadoPostulacion getEstado() {
		return estado;
	}

	public void setEstado(EstadoPostulacion estado) {
		this.estado = estado;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public List<?> getExperiencias() {
		return experiencias;
	}

	public void setExperiencias(List<?> experiencias) {
		this.experiencias = experiencias;
	}

	public List<?> getCertificaciones() {
		return certificaciones;
	}

	public void setCertificaciones(List<?> certificaciones) {
		this.certificaciones = certificaciones;
	}

	public String getCvUrl() {
		return cvUrl;
	}

	public void setCvUrl(String cvUrl) {
		this.cvUrl = cvUrl;
	}
	
}