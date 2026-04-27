import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {
  AuthService,
  RegisterRequest
} from '../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  /**
   * Formulario único de registro.
   * Contiene credenciales + datos de perfil
   * El backend decide qué perfil crear según el role.
   */
  form: RegisterRequest = {
    email: '',
    password: '',
    role: 'POSTULANTE',

    // Campos POSTULANTE
    nombre: '',
    apellidos: '',
    bio: '',

    // Campos EMPRESA
    nombreEmpresa: '',
    descripcion: '',

    // Campos COMUNES
    ubicacion: '',
    telefono: ''
  };

  loading = false;
  errorMessage: string | null = null;

  constructor(private authService: AuthService) {}

  /**
   * Envío del registro.
   * ✅ Una sola llamada
   * ✅ El backend crea User + Perfil correcto
   */
  onRegister(): void {
    this.loading = true;
    this.errorMessage = null;

    this.authService.register(this.form).subscribe({
      next: () => {
        // La redirección se gestiona en AuthService.handleAuthSuccess
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'No se pudo completar el registro';
        this.loading = false;
      }
    });
  }
}
