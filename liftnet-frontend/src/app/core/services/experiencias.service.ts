import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { Experiencia } from './postulante.service';
import { ApiResponse } from '../models/api-response.model';

@Injectable({
  providedIn: 'root'
})
export class ExperienciasService {

  private readonly apiUrl = `${environment.apiUrl}/experiencias`;

  constructor(private http: HttpClient) {}

  getMisExperiencias(page = 0, size = 50): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(`${this.apiUrl}/mis-experiencias`, { params });
  }

  addExperiencia(experiencia: Experiencia): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(this.apiUrl, experiencia);
  }

  deleteExperiencia(id: any): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }
}
