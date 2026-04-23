import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService, LoginRequest } from '../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <h2>Registro</h2>

    <form (ngSubmit)="onRegister()" #registerForm="ngForm">
      <div>
        <label>Email</label><br />
        <input
          type="email"
          name="email"
          [(ngModel)]="credentials.email"
          required
        />
      </div>

      <div>
        <label>Password</label><br />
        <input
          type="password"
          name="password"
          [(ngModel)]="credentials.password"
          required
        />
      </div>

      <button type="submit" [disabled]="registerForm.invalid || loading">
        Registrar
      </button>
    </form>

    <p *ngIf="errorMessage" style="color: red;">
      {{ errorMessage }}
    </p>
  `
})
export class RegisterComponent {

  credentials: LoginRequest = {
    email: '',
    password: ''
  };

  loading = false;
  errorMessage: string | null = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onRegister(): void {
    this.loading = true;
    this.errorMessage = null;

    this.authService.register(this.credentials).subscribe({
      next: (response) => {
        if (response.success) {
          // Registro correcto → el backend devuelve token
          this.router.navigate(['/certificaciones']);
        } else {
          this.errorMessage = response.message ?? 'Error en registro';
        }
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'No se pudo completar el registro';
        this.loading = false;
      }
    });
  }
}
