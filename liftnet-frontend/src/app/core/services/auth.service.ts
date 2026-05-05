import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

import { environment } from '../../../environments/environment.development';
import { ApiResponse } from '../models/api-response.model';
import { TokenStorageService } from './token-storage.service';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterUserRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  accessToken: string;
  role: 'POSTULANTE' | 'EMPRESA' | null;
  profileCompleted: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly apiUrl = `${environment.apiUrl}/auth`;

  constructor(
    private http: HttpClient,
    private tokenStorage: TokenStorageService,
    private router: Router
  ) { }

  // LOGIN
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

  // REGISTER – PASO 1 (crear solo el usuario)
  register(request: {
    email: string;
    password: string;
    role: 'POSTULANTE' | 'EMPRESA';
  }): Observable<ApiResponse<AuthResponse>> {
    return this.http
      .post<ApiResponse<AuthResponse>>(`${this.apiUrl}/register`, request)
      .pipe(
        tap(res => {
          if (res.success) {
            this.tokenStorage.saveToken(res.data.accessToken);
            this.tokenStorage.saveEmail(request.email);
            this.tokenStorage.saveRole(res.data.role);
            this.tokenStorage.saveProfileCompleted(false);
          }
        })
      );
  }

  // PASO 2 – Guardar rol
  setRole(role: 'POSTULANTE' | 'EMPRESA'): Observable<void> {
    return this.http
      .post<void>(`${this.apiUrl}/set-role`, { role })
      .pipe(
        tap(() => {
          this.tokenStorage.saveRole(role);
          this.tokenStorage.saveProfileCompleted(false);
        })
      );
  }

  // LOGIN / REDIRECCIÓN FINAL
  private handleAuthSuccess(email: string, auth: AuthResponse): void {
    this.tokenStorage.saveToken(auth.accessToken);
    this.tokenStorage.saveEmail(email);
    this.tokenStorage.saveRole(auth.role);
    this.tokenStorage.saveProfileCompleted(auth.profileCompleted);

    if (!auth.role) {
      return;
    }

    if (!auth.profileCompleted) {
      if (auth.role === 'POSTULANTE') {
        this.router.navigate(['/postulante/perfil']);
      }
      if (auth.role === 'EMPRESA') {
        this.router.navigate(['/empresa/perfil']);
      }
      return;
    }

    if (auth.role === 'POSTULANTE') {
      this.router.navigate(['/postulante/postulaciones']);
    }

    if (auth.role === 'EMPRESA') {
      this.router.navigate(['/empresa/ofertas/mis-ofertas']);
    }
  }

  logout(): void {
    this.tokenStorage.clear();
    this.router.navigate(['/login']);
  }
}
