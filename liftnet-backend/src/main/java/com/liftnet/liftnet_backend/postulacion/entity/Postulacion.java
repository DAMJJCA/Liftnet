package com.liftnet.liftnet_backend.postulacion.entity;

import com.liftnet.liftnet_backend.oferta.entity.OfertaTrabajo;
import com.liftnet.liftnet_backend.postulante.entity.PostulanteProfile;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "postulacion",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"oferta_id", "postulante_id"})
    }
)
public class Postulacion {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "oferta_id")
    private OfertaTrabajo oferta;

    @ManyToOne(optional = false)
    @JoinColumn(name = "postulante_id")
    private PostulanteProfile postulante;

    @Enumerated(EnumType.STRING)
    private EstadoPostulacion estado = EstadoPostulacion.PENDIENTE;

    private Instant createdAt = Instant.now();

    public UUID getId() {
        return id;
    }

    public OfertaTrabajo getOferta() {
        return oferta;
    }

    public void setOferta(OfertaTrabajo oferta) {
        this.oferta = oferta;
    }

    public PostulanteProfile getPostulante() {
        return postulante;
    }

    public void setPostulante(PostulanteProfile postulante) {
        this.postulante = postulante;
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
}