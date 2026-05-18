package com.liftnet.liftnet_backend.postulante.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liftnet.liftnet_backend.certificacion.entity.PostulanteCertificacion;
import com.liftnet.liftnet_backend.experiencia.entity.Experiencia;
import com.liftnet.liftnet_backend.user.entity.User;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "postulante_profile")
public class PostulanteProfile {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @Column(name = "foto_url", columnDefinition = "TEXT")
    private String fotoUrl; 

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String apellidos;

    private String ubicacion;

    @Column(length = 20)
    private String telefono;

    @Column(length = 500)
    private String bio;
    
    @OneToMany(mappedBy = "postulante")
    @JsonIgnore
    private List<Experiencia> experiencias;
    
    @OneToMany(mappedBy = "postulante")
    @JsonIgnore
    private List<PostulanteCertificacion> certificaciones;

    @Column(name = "cv_url", columnDefinition = "TEXT")
    private String cvUrl;
    
    private boolean disponible = true;

    private Instant createdAt = Instant.now();

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
    public String getFotoUrl() { 
    	return fotoUrl; 
    }
    public void setFotoUrl(String fotoUrl) { 
    	this.fotoUrl = fotoUrl; 
    }
    public List<Experiencia> getExperiencias() {
        return experiencias;
    }

    public void setExperiencias(List<Experiencia> experiencias) {
        this.experiencias = experiencias;
    }

	public List<PostulanteCertificacion> getCertificaciones() {
		return certificaciones;
	}

	public void setCertificaciones(List<PostulanteCertificacion> certificaciones) {
		this.certificaciones = certificaciones;
	}

	public String getCvUrl() {
		return cvUrl;
	}

	public void setCvUrl(String cvUrl) {
		this.cvUrl = cvUrl;
	}
}