import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment.development';
import { ApiResponse } from '../models/api-response.model';

//   MODELOS

export interface CertificacionPostulante {
  id: string;
  certificacionId: string;
  nombre: string;
  entidad: string | null;
  fechaObtencion: string | null;
  fechaExpiracion: string | null;
}

// Respuesta paginada de Spring Data REST
export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

//   SERVICIO de certificaciones
@Injectable({
  providedIn: 'root'
})
export class CertificacionesService {

  private readonly apiUrl =
    `${environment.apiUrl}/certificaciones`;

  constructor(private http: HttpClient) {}

  //  Mis certificaciones ya esta paginada, no hace falta un endpoint aparte para eso
  getMisCertificaciones(
    page: number,
    size: number
  ): Observable<ApiResponse<PageResponse<CertificacionPostulante>>> {

    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<ApiResponse<PageResponse<CertificacionPostulante>>>(
      `${this.apiUrl}/mis-certificaciones`,
      { params }
    );
  }

  //  Asignar certificación a postulante
  asignarCertificacion(body: {
    certificacionId: string;
    fechaObtencion?: string;
    fechaExpiracion?: string;
  }): Observable<ApiResponse<void>> {

    return this.http.post<ApiResponse<void>>(
      `${this.apiUrl}/asignar`,
      body
    );
  }

  //  Eliminar certificación a postulante
  eliminarCertificacion(id: string): Observable<ApiResponse<void>> {

    return this.http.delete<ApiResponse<void>>(
      `${this.apiUrl}/${id}`
    );
  }
}
