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

    // Datos extra de la oferta y empresa
    private String nombreEmpresa;
    private String ubicacionOferta;
    private String descripcionOferta;
    private String telefonoEmpresa;
    private String emailEmpresa;

    public PostulacionResponse(
            UUID id, UUID ofertaId, String tituloOferta, EstadoPostulacion estado, Instant createdAt,
            String nombreEmpresa, String ubicacionOferta, String descripcionOferta, 
            String telefonoEmpresa, String emailEmpresa) {

        this.id = id;
        this.ofertaId = ofertaId;
        this.tituloOferta = tituloOferta;
        this.estado = estado;
        this.createdAt = createdAt;
        this.nombreEmpresa = nombreEmpresa;
        this.ubicacionOferta = ubicacionOferta;
        this.descripcionOferta = descripcionOferta;
        this.telefonoEmpresa = telefonoEmpresa;
        this.emailEmpresa = emailEmpresa;
    }

    // GETTERS
    public UUID getId() { return id; }
    public UUID getOfertaId() { return ofertaId; }
    public String getTituloOferta() { return tituloOferta; }
    public EstadoPostulacion getEstado() { return estado; }
    public Instant getCreatedAt() { return createdAt; }
    public String getNombreEmpresa() { return nombreEmpresa; }
    public String getUbicacionOferta() { return ubicacionOferta; }
    public String getDescripcionOferta() { return descripcionOferta; }
    public String getTelefonoEmpresa() { return telefonoEmpresa; }
    public String getEmailEmpresa() { return emailEmpresa; }

	public void setId(UUID id) {
		this.id = id;
	}

	public void setOfertaId(UUID ofertaId) {
		this.ofertaId = ofertaId;
	}

	public void setTituloOferta(String tituloOferta) {
		this.tituloOferta = tituloOferta;
	}

	public void setEstado(EstadoPostulacion estado) {
		this.estado = estado;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}

	public void setUbicacionOferta(String ubicacionOferta) {
		this.ubicacionOferta = ubicacionOferta;
	}

	public void setDescripcionOferta(String descripcionOferta) {
		this.descripcionOferta = descripcionOferta;
	}

	public void setTelefonoEmpresa(String telefonoEmpresa) {
		this.telefonoEmpresa = telefonoEmpresa;
	}

	public void setEmailEmpresa(String emailEmpresa) {
		this.emailEmpresa = emailEmpresa;
	}
    
    
}