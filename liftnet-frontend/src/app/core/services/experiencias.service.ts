import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment.development';
import { ApiResponse } from '../models/api-response.model';

/* ========================
   MODELOS
   ======================== */

export interface Experiencia {
  id: string;
  lugar: string;
  descripcion: string | null;
  fechaInicio: string | null;
  fechaFin: string | null;
}

/* Respuesta paginada de Spring */
export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

/* ========================
   SERVICE
   ======================== */

@Injectable({
  providedIn: 'root'
})
export class ExperienciasService {

  private readonly apiUrl =
    `${environment.apiUrl}/experiencias`;

  constructor(private http: HttpClient) {}

  /* ========================
     GET: Mis experiencias (paginado)
     ======================== */
  getMisExperiencias(
    page: number,
    size: number
  ): Observable<ApiResponse<PageResponse<Experiencia>>> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<ApiResponse<PageResponse<Experiencia>>>(
      `${this.apiUrl}/mis-experiencias`,
      { params }
    );
  }

  /* ========================
     POST: Crear experiencia
     ======================== */
  crearExperiencia(body: {
    lugar: string;
    descripcion?: string;
    fechaInicio?: string;
    fechaFin?: string;
  }): Observable<ApiResponse<void>> {

    return this.http.post<ApiResponse<void>>(
      `${this.apiUrl}`,
      body
    );
  }

  /* ========================
     PUT: Actualizar experiencia
     ======================== */
  actualizarExperiencia(
    id: string,
    body: {
      lugar: string;
      descripcion?: string;
      fechaInicio?: string;
      fechaFin?: string;
    }
  ): Observable<ApiResponse<void>> {

    return this.http.put<ApiResponse<void>>(
      `${this.apiUrl}/${id}`,
      body
    );
  }

  /* ========================
     DELETE: Eliminar experiencia
     ======================== */
  eliminarExperiencia(id: string): Observable<ApiResponse<void>> {

    return this.http.delete<ApiResponse<void>>(
      `${this.apiUrl}/${id}`
    );
  }
}
