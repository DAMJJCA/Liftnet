import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment.development';
import { ApiResponse } from '../models/api-response.model';

export interface Postulacion {
  postulacionId: string;
  ofertaId: string;
  tituloOferta: string;
  estado: string;
  createdAt: string;

  nombreEmpresa?: string;
  descripcionOferta?: string;
  ubicacionOferta?: string;
  telefonoEmpresa?: string;
  emailEmpresa?: string;
}

@Injectable({
  providedIn: 'root'
})
export class PostulacionService {

  private readonly apiUrl = `${environment.apiUrl}/postulaciones`;

  constructor(private http: HttpClient) { }

  postular(ofertaId: string): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(`${this.apiUrl}/oferta/${ofertaId}`, {});
  }

  getMisPostulaciones(page = 0, size = 10): Observable<ApiResponse<any>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<any>>(`${this.apiUrl}/mis-postulaciones`, { params });
  }

  getPostulacionesOferta(ofertaId: string, page = 0, size = 10): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(`${this.apiUrl}/oferta/${ofertaId}`, { params });
  }

  actualizarEstado(postulacionId: string, estado: 'ACEPTADA' | 'RECHAZADA'): Observable<any> {
    const params = new HttpParams().set('estado', estado);
    return this.http.put<any>(`${this.apiUrl}/${postulacionId}/estado`, {}, { params });
  }
}
