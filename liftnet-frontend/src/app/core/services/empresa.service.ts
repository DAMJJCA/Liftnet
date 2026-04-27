import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment.development';
import { ApiResponse } from '../models/api-response.model';
import { TokenStorageService } from './token-storage.service';

export interface EmpresaProfile {
  nombreEmpresa: string;
  ubicacion: string;
  telefono: string;
  descripcion: string;
}

@Injectable({
  providedIn: 'root'
})
export class EmpresaService {

  private readonly apiUrl = `${environment.apiUrl}/empresa/profile`;

  constructor(
    private http: HttpClient,
    private tokenStorage: TokenStorageService
  ) {}

  // ==========================
  // READ – VER PERFIL
  // ==========================
  getProfile(): Observable<ApiResponse<EmpresaProfile>> {
    const email = this.tokenStorage.getEmail();
    return this.http.get<ApiResponse<EmpresaProfile>>(
      `${this.apiUrl}?email=${email}`
    );
  }

  // ==========================
  // CREATE – CREAR PERFIL (solo DEV/manual)
  // ==========================
  createProfile(
    data: EmpresaProfile
  ): Observable<ApiResponse<EmpresaProfile>> {
    const email = this.tokenStorage.getEmail();
    return this.http.post<ApiResponse<EmpresaProfile>>(
      `${this.apiUrl}?email=${email}`,
      data
    );
  }

  // ==========================
  // UPDATE – EDITAR PERFIL
  // ==========================
  updateProfile(
    data: EmpresaProfile
  ): Observable<ApiResponse<EmpresaProfile>> {
    const email = this.tokenStorage.getEmail();
    return this.http.put<ApiResponse<EmpresaProfile>>(
      `${this.apiUrl}?email=${email}`,
      data
    );
  }
}
