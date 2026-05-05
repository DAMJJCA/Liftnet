import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  form = {
    email: '',
    password: '',
    confirmPassword: '',
    role: undefined as 'POSTULANTE' | 'EMPRESA' | undefined
  };

  loading = false;
  errorMessage: string | null = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onRegister(): void {
    this.errorMessage = null;

    if (this.form.password !== this.form.confirmPassword) {
      this.errorMessage = 'Las contraseñas no coinciden';
      return;
    }

    if (!this.form.role) {
      this.errorMessage = 'Selecciona el tipo de cuenta';
      return;
    }

    this.loading = true;

    this.authService.register({
      email: this.form.email,
      password: this.form.password,
      role: this.form.role
    }).subscribe({
      next: () => {
        if (this.form.role === 'POSTULANTE') {
          this.router.navigate(['/postulante/perfil']);
        } else {
          this.router.navigate(['/empresa/perfil']);
        }
      },
      error: () => {
        this.errorMessage = 'No se pudo completar el registro';
        this.loading = false;
      }
    });
  }
}
