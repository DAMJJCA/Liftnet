package com.liftnet.liftnet_backend.oferta.entity;

import com.liftnet.liftnet_backend.empresa.entity.EmpresaProfile;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "oferta_trabajo")
public class OfertaTrabajo {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id")
    private EmpresaProfile empresa;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(length = 500)
    private String descripcion;

    @Column(length = 150)
    private String ubicacion;

    private boolean activa = true;

    private Instant createdAt = Instant.now();

    public UUID getId() {
        return id;
    }

    public EmpresaProfile getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaProfile empresa) {
        this.empresa = empresa;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}