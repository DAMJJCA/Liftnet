import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment.development';
import { ApiResponse } from '../models/api-response.model';

export interface Experiencia {
  id?: number;
  lugar?: string;
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

  constructor(private http: HttpClient) {}

  getProfile(): Observable<ApiResponse<PostulanteProfile>> {
    return this.http.get<ApiResponse<PostulanteProfile>>(this.apiUrl);
  }

  createProfile(data: PostulanteProfile): Observable<ApiResponse<PostulanteProfile>> {
    return this.http.post<ApiResponse<PostulanteProfile>>(this.apiUrl, data);
  }

  updateProfile(data: PostulanteProfile): Observable<ApiResponse<PostulanteProfile>> {
    return this.http.put<ApiResponse<PostulanteProfile>>(this.apiUrl, data);
  }

  updateDisponibilidad(disponible: boolean): Observable<void> {
    const params = new HttpParams().set('disponible', disponible);
    return this.http.patch<void>(`${this.apiUrl}/disponibilidad`, {}, { params });
  }
}
