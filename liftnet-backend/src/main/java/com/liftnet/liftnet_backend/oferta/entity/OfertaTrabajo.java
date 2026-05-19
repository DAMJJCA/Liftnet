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

    @Column(name = "estudios_minimos")
    private String estudiosMinimos;

    @Column(name = "experiencia_minima")
    private String experienciaMinima;

    private String idiomas;

    private String nivel;

    private Integer vacantes = 1;

    private String salario;

    private boolean activa = true;

    private Instant createdAt = Instant.now();

    // GETTERS Y SETTERS
    public UUID getId() { return id; }
    public EmpresaProfile getEmpresa() { return empresa; }
    public void setEmpresa(EmpresaProfile empresa) { this.empresa = empresa; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
    public Instant getCreatedAt() { return createdAt; }

    // GETTERS Y SETTERS DE LOS NUEVOS CAMPOS
    public String getEstudiosMinimos() { return estudiosMinimos; }
    public void setEstudiosMinimos(String estudiosMinimos) { this.estudiosMinimos = estudiosMinimos; }
    public String getExperienciaMinima() { return experienciaMinima; }
    public void setExperienciaMinima(String experienciaMinima) { this.experienciaMinima = experienciaMinima; }
    public String getIdiomas() { return idiomas; }
    public void setIdiomas(String idiomas) { this.idiomas = idiomas; }
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
    public Integer getVacantes() { return vacantes; }
    public void setVacantes(Integer vacantes) { this.vacantes = vacantes; }
    public String getSalario() { return salario; }
    public void setSalario(String salario) { this.salario = salario; }
}