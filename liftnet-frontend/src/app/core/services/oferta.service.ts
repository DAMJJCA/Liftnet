import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment.development';

export interface Oferta {
  id: string;
  titulo: string;
  descripcion: string;
  ubicacion: string;
  activa: boolean;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class OfertaService {

  private readonly apiUrl = `${environment.apiUrl}/ofertas`;

  constructor(private http: HttpClient) {}

  // EMPRESA

  getMisOfertas(page = 0, size = 10): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get(`${this.apiUrl}/mis-ofertas`, { params });
  }

  crearOferta(oferta: { titulo: string; descripcion: string; ubicacion: string; }): Observable<any> {
    return this.http.post(this.apiUrl, oferta);
  }

  editarOferta(ofertaId: string, oferta: { titulo: string; descripcion: string; ubicacion: string; }): Observable<any> {
    return this.http.put(`${this.apiUrl}/${ofertaId}`, oferta);
  }

  cerrarOferta(ofertaId: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/${ofertaId}/cerrar`, {});
  }

  // POSTULANTE

  getOfertasActivas(ubicacion?: string, page = 0, size = 10): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (ubicacion) {
      params = params.set('ubicacion', ubicacion);
    }
    return this.http.get(this.apiUrl, { params });
  }
}
