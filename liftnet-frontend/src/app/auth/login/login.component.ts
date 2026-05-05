import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { AuthService, LoginRequest } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  credentials: LoginRequest = {
    email: '',
    password: ''
  };

  loading = false;
  errorMessage: string | null = null;

  constructor(private authService: AuthService) {}

  onLogin(): void {
    this.loading = true;
    this.errorMessage = null;

    this.authService.login(this.credentials).subscribe({
      next: () => {
        // La navegación la hace AuthService
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Credenciales incorrectas';
        this.loading = false;
      }
    });
  }
}
