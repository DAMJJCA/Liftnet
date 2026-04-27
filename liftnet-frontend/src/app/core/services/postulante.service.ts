import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment.development';
import { ApiResponse } from '../models/api-response.model';
import { TokenStorageService } from './token-storage.service';

// ==========================
// MODELOS DEL PERFIL
// ==========================
export interface Experiencia {
  id?: number;
  puesto?: string;
  empresa?: string;
  descripcion?: string;
  fechaInicio?: string;
  fechaFin?: string;
}

export interface Certificacion {
  id?: number;
  nombre: string;
  entidad?: string;
  fecha?: string;
}

// ==========================
// PERFIL POSTULANTE
// ==========================
export interface PostulanteProfile {
  nombre: string;
  apellidos: string;
  email?: string;
  ubicacion: string;
  telefono: string;
  bio: string;
  disponible: boolean;

  experiencias: Experiencia[];
  certificaciones: Certificacion[];
}

@Injectable({
  providedIn: 'root'
})
export class PostulanteService {

  private readonly apiUrl = `${environment.apiUrl}/postulante/profile`;

  constructor(
    private http: HttpClient,
    private tokenStorage: TokenStorageService
  ) {}

  // ==========================
  // READ – VER PERFIL
  // ==========================
  getProfile(): Observable<ApiResponse<PostulanteProfile>> {
    const email = this.tokenStorage.getEmail();
    return this.http.get<ApiResponse<PostulanteProfile>>(
      `${this.apiUrl}?email=${email}`
    );
  }

  // ==========================
  // CREATE – CREAR PERFIL
  // ==========================
  createProfile(
    data: PostulanteProfile
  ): Observable<ApiResponse<PostulanteProfile>> {
    const email = this.tokenStorage.getEmail();
    return this.http.post<ApiResponse<PostulanteProfile>>(
      `${this.apiUrl}?email=${email}`,
      data
    );
  }

  // ==========================
  // UPDATE – EDITAR PERFIL
  // ==========================
  updateProfile(
    data: PostulanteProfile
  ): Observable<ApiResponse<PostulanteProfile>> {
    const email = this.tokenStorage.getEmail();
    return this.http.put<ApiResponse<PostulanteProfile>>(
      `${this.apiUrl}?email=${email}`,
      data
    );
  }

  // ==========================
  // UPDATE – DISPONIBILIDAD
  // ==========================
  updateDisponibilidad(disponible: boolean): Observable<void> {
    const email = this.tokenStorage.getEmail();
    return this.http.patch<void>(
      `${this.apiUrl}/disponibilidad?email=${email}&disponible=${disponible}`,
      {}
    );
  }
}
