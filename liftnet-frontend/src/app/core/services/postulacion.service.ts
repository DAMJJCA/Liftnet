import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment.development';
import { ApiResponse } from '../models/api-response.model';
import { TokenStorageService } from './token-storage.service';

export interface Postulacion {
  id: string;
  ofertaId: string;
  tituloOferta: string;
  estado: string;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class PostulacionService {

  private readonly apiUrl = `${environment.apiUrl}/postulaciones`;

  constructor(
    private http: HttpClient,
    private tokenStorage: TokenStorageService
  ) { }

  // ==========================
  // POSTULANTE → POSTULARSE
  // ==========================
  postular(ofertaId: string): Observable<ApiResponse<void>> {
    const email = this.tokenStorage.getEmail();
    return this.http.post<ApiResponse<void>>(
      `${this.apiUrl}/oferta/${ofertaId}?email=${email}`,
      {}
    );
  }

  // ==========================
  // POSTULANTE → VER MIS POSTULACIONES
  // ==========================
  getMisPostulaciones(page = 0, size = 10): Observable<ApiResponse<any>> {
    const email = this.tokenStorage.getEmail();
    return this.http.get<ApiResponse<any>>(
      `${this.apiUrl}/mis-postulaciones?email=${email}&page=${page}&size=${size}`
    );
  }
  // ==========================
  // EMPRESA → VER POSTULACIONES DE UNA OFERTA
  // ==========================
  getPostulacionesOferta(
    ofertaId: string,
    page = 0,
    size = 10
  ): Observable<any> {
    const email = this.tokenStorage.getEmail();
    return this.http.get<any>(
      `${this.apiUrl}/oferta/${ofertaId}?email=${email}&page=${page}&size=${size}`
    );
  }

  // ==========================
  // EMPRESA → ACEPTAR / RECHAZAR
  // ==========================
  actualizarEstado(
    postulacionId: string,
    estado: 'ACEPTADA' | 'RECHAZADA'
  ): Observable<any> {
    const email = this.tokenStorage.getEmail();
    return this.http.put<any>(
      `${this.apiUrl}/${postulacionId}/estado?email=${email}&estado=${estado}`,
      {}
    );
  }
}
