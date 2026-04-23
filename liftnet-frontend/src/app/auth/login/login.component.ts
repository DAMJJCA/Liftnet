import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService, LoginRequest } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <h2>Login</h2>

    <form (ngSubmit)="onLogin()" #loginForm="ngForm">
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

      <button type="submit" [disabled]="loginForm.invalid || loading">
        Login
      </button>
    </form>

    <p *ngIf="errorMessage" style="color: red;">
      {{ errorMessage }}
    </p>
  `,
})
export class LoginComponent {

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

  onLogin(): void {
    this.loading = true;
    this.errorMessage = null;

    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        if (response.success) {
          // Login correcto → redirigir
          this.router.navigate(['/certificaciones']);
        } else {
          this.errorMessage = response.message ?? 'Login incorrecto';
        }
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Credenciales inválidas';
        this.loading = false;
      }
    });
  }
}
