import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';

export interface Postulacion {
  id: string;
  ofertaId: string;
  tituloOferta: string;
  estado: string;
  createdAt: string;
  nombreEmpresa?: string;
  ubicacionOferta?: string;
  descripcionOferta?: string;
  telefonoEmpresa?: string;
  emailEmpresa?: string;
}

@Injectable({
  providedIn: 'root'
})
export class PostulacionService {

  private readonly apiUrl = `${environment.apiUrl}/postulaciones`;

  constructor(private http: HttpClient) {}

  // POSTULANTE: Postularse a una oferta
  postular(ofertaId: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/oferta/${ofertaId}`, {});
  }

  // POSTULANTE: Ver a qué ofertas ha aplicado
  getMisPostulaciones(page = 0, size = 20): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get(`${this.apiUrl}/mis-postulaciones`, { params });
  }

  // EMPRESA: Ver los candidatos de una oferta específica
  getPostulacionesOferta(ofertaId: string, page = 0, size = 20): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get(`${this.apiUrl}/oferta/${ofertaId}`, { params });
  }

  // EMPRESA: Aceptar o rechazar a un candidato
  actualizarEstado(postulacionId: string, estado: 'ACEPTADA' | 'RECHAZADA'): Observable<any> {
    const params = new HttpParams().set('estado', estado);
    return this.http.put(`${this.apiUrl}/${postulacionId}/estado`, {}, { params });
  }
}
