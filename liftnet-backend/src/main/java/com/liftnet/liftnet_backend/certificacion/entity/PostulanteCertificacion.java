package com.liftnet.liftnet_backend.certificacion.entity;

import com.liftnet.liftnet_backend.postulante.entity.PostulanteProfile;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "postulante_certificacion")
public class PostulanteCertificacion {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postulante_id", nullable = false)
    private PostulanteProfile postulante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificacion_id", nullable = false)
    private Certificacion certificacion;

    private LocalDate fechaObtencion;
    private LocalDate fechaExpiracion;

    private LocalDateTime createdAt;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public PostulanteProfile getPostulante() {
		return postulante;
	}

	public void setPostulante(PostulanteProfile postulante) {
		this.postulante = postulante;
	}

	public Certificacion getCertificacion() {
		return certificacion;
	}

	public void setCertificacion(Certificacion certificacion) {
		this.certificacion = certificacion;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
