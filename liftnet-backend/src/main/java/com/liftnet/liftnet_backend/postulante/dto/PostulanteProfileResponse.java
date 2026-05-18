package com.liftnet.liftnet_backend.postulante.dto;

public class PostulanteProfileResponse {

    private String nombre;
    private String apellidos;
    private String ubicacion;
    private String telefono;
    private String bio;
    private boolean disponible;
    private String fotoUrl;
    private String cvUrl;

    public PostulanteProfileResponse(
            String nombre,
            String apellidos,
            String ubicacion,
            String telefono,
            String bio,
            boolean disponible,
            String fotoUrl,
            String cvUrl) {

        this.nombre = nombre;
        this.apellidos = apellidos;
        this.ubicacion = ubicacion;
        this.telefono = telefono;
        this.bio = bio;
        this.disponible = disponible;
        this.fotoUrl = fotoUrl;
        this.cvUrl=cvUrl;
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

	public String getFotoUrl() {
		return fotoUrl;
	}

	public void setFotoUrl(String fotoUrl) {
		this.fotoUrl = fotoUrl;
	}

	public String getCvUrl() {
		return cvUrl;
	}

	public void setCvUrl(String cvUrl) {
		this.cvUrl = cvUrl;
	}
	
}