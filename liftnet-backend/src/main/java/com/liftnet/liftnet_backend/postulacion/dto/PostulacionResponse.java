package com.liftnet.liftnet_backend.postulacion.dto;

import com.liftnet.liftnet_backend.postulacion.entity.EstadoPostulacion;

import java.time.Instant;
import java.util.UUID;

public class PostulacionResponse {

    private UUID id;
    private UUID ofertaId;
    private String tituloOferta;
    private EstadoPostulacion estado;
    private Instant createdAt;

    public PostulacionResponse(
            UUID id,
            UUID ofertaId,
            String tituloOferta,
            EstadoPostulacion estado,
            Instant createdAt) {

        this.id = id;
        this.ofertaId = ofertaId;
        this.tituloOferta = tituloOferta;
        this.estado = estado;
        this.createdAt = createdAt;
    }

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getOfertaId() {
		return ofertaId;
	}

	public void setOfertaId(UUID ofertaId) {
		this.ofertaId = ofertaId;
	}

	public String getTituloOferta() {
		return tituloOferta;
	}

	public void setTituloOferta(String tituloOferta) {
		this.tituloOferta = tituloOferta;
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
}