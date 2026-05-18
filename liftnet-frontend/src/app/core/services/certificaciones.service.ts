import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment.development';
import { ApiResponse } from '../models/api-response.model';

export interface CertificacionPostulante {
  id: string;
  certificacionId: string;
  nombre: string;
  entidad: string | null;
  fechaObtencion: string | null;
  fechaExpiracion: string | null;
  archivoUrl?: string | null;
}

export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

@Injectable({
  providedIn: 'root'
})
export class CertificacionesService {

  private readonly apiUrl = `${environment.apiUrl}/certificaciones`;

  constructor(private http: HttpClient) {}

  getMisCertificaciones(page = 0, size = 10): Observable<ApiResponse<PageResponse<CertificacionPostulante>>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<PageResponse<CertificacionPostulante>>>(
      `${this.apiUrl}/mis-certificaciones`,
      { params }
    );
  }

  asignarCertificacion(body: {
    certificacionId: string;
    fechaObtencion?: string;
    fechaExpiracion?: string;
    archivoUrl?: string;
  }): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(`${this.apiUrl}/asignar`, body);
  }

  eliminarCertificacion(id: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }
}
