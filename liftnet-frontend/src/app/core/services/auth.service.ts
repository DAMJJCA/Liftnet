import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

import { environment } from '../../../environments/environment.development';
import { ApiResponse } from '../models/api-response.model';
import { TokenStorageService } from './token-storage.service';
import { UiStateService } from './ui-state.service';

// ==========================
// MODELOS
// ==========================

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  role: 'POSTULANTE' | 'EMPRESA';

  nombre?: string;
  apellidos?: string;
  bio?: string;

  ubicacion?: string;
  telefono?: string;

  nombreEmpresa?: string;
  descripcion?: string;
}

export interface AuthResponse {
  accessToken: string;
  role: string;
  profileCompleted: boolean;
}

// ==========================
// SERVICE
// ==========================

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly apiUrl = `${environment.apiUrl}/auth`;

  constructor(
    private http: HttpClient,
    private tokenStorage: TokenStorageService,
    private router: Router,
    private uiState: UiStateService
  ) {}

  // ---------- LOGIN ----------
  login(request: LoginRequest): Observable<ApiResponse<AuthResponse>> {
    return this.http
      .post<ApiResponse<AuthResponse>>(`${this.apiUrl}/login`, request)
      .pipe(
        tap(res => {
          if (res.success) {
            this.handleAuthSuccess(request.email, res.data);
          }
        })
      );
  }

  // ---------- REGISTER ----------
  register(request: RegisterRequest): Observable<ApiResponse<AuthResponse>> {
    return this.http
      .post<ApiResponse<AuthResponse>>(`${this.apiUrl}/register`, request)
      .pipe(
        tap(res => {
          if (res.success) {
            this.handleAuthSuccess(request.email, res.data);
          }
        })
      );
  }

  // ==========================
  // REDIRECCIÓN SEGÚN ROL
  // ==========================
  private handleAuthSuccess(email: string, auth: AuthResponse): void {
    this.tokenStorage.saveToken(auth.accessToken);
    this.tokenStorage.saveRole(auth.role);
    this.tokenStorage.saveEmail(email);
    this.tokenStorage.saveProfileCompleted(auth.profileCompleted);

    // 🚨 PERFIL OBLIGATORIO
    if (!auth.profileCompleted) {
      this.uiState.setProfileMessage(
        'Para continuar, debes completar tu perfil'
      );

      if (auth.role === 'POSTULANTE') {
        this.router.navigate(['/postulante/perfil']);
        return;
      }

      if (auth.role === 'EMPRESA') {
        this.router.navigate(['/empresa/perfil']);
        return;
      }
    }

    // PERFIL COMPLETO → HOME SEGÚN ROL
    switch (auth.role) {
      case 'POSTULANTE':
        // TODAS SUS POSTULACIONES
        this.router.navigate(['/postulante/postulaciones']);
        break;

      case 'EMPRESA':
        // TODAS SUS OFERTAS
        this.router.navigate(['/ofertas/mis-ofertas']);
        break;

      case 'ADMIN':
        this.router.navigate(['/admin']);
        break;

      default:
        this.router.navigate(['/login']);
    }
  }

  logout(): void {
    this.tokenStorage.clear();
    this.router.navigate(['/login']);
  }
}
