	package com.liftnet.liftnet_backend.auth.dto;
	
	import jakarta.validation.constraints.Email;
	import jakarta.validation.constraints.NotBlank;
	
	public class RegisterRequest {
	
	    @Email
	    @NotBlank
	    private String email;
	
	    @NotBlank
	    private String password;
	    
	
	    @NotBlank
	    private String role;// POSTULANTE | EMPRESA
	
	
		public String getEmail() {
			return email;
		}
	
	
		public void setEmail(String email) {
			this.email = email;
		}
	
	
		public String getPassword() {
			return password;
		}
	
	
		public void setPassword(String password) {
			this.password = password;
		}
	
	
		public String getRole() {
			return role;
		}
	
	
		public void setRole(String role) {
			this.role = role;
		}
	}
	