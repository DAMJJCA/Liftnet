import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment.development';
import { ApiResponse } from '../models/api-response.model';

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

  // ¡Mira qué limpio! Ya no inyectamos TokenStorageService
  constructor(private http: HttpClient) {}

  getProfile(): Observable<ApiResponse<EmpresaProfile>> {
    return this.http.get<ApiResponse<EmpresaProfile>>(this.apiUrl);
  }

  createProfile(data: EmpresaProfile): Observable<ApiResponse<EmpresaProfile>> {
    return this.http.post<ApiResponse<EmpresaProfile>>(this.apiUrl, data);
  }

  updateProfile(data: EmpresaProfile): Observable<ApiResponse<EmpresaProfile>> {
    return this.http.put<ApiResponse<EmpresaProfile>>(this.apiUrl, data);
  }
}
