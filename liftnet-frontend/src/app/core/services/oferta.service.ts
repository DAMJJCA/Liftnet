import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment.development';
import { TokenStorageService } from './token-storage.service';

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

  constructor(
    private http: HttpClient,
    private tokenStorage: TokenStorageService
  ) {}

  // ==========================
  // EMPRESA
  // ==========================

  getMisOfertas(page = 0, size = 10): Observable<any> {
    const email = this.tokenStorage.getEmail();
    return this.http.get(
      `${this.apiUrl}/mis-ofertas?email=${email}&page=${page}&size=${size}`
    );
  }

  crearOferta(oferta: {
    titulo: string;
    descripcion: string;
    ubicacion: string;
  }): Observable<any> {
    const email = this.tokenStorage.getEmail();
    return this.http.post(
      `${this.apiUrl}?email=${email}`,
      oferta
    );
  }

  editarOferta(
    ofertaId: string,
    oferta: {
      titulo: string;
      descripcion: string;
      ubicacion: string;
    }
  ): Observable<any> {
    const email = this.tokenStorage.getEmail();
    return this.http.put(
      `${this.apiUrl}/${ofertaId}?email=${email}`,
      oferta
    );
  }

  cerrarOferta(ofertaId: string): Observable<any> {
    const email = this.tokenStorage.getEmail();
    return this.http.put(
      `${this.apiUrl}/${ofertaId}/cerrar?email=${email}`,
      {}
    );
  }

  // ==========================
  // POSTULANTE
  // ==========================

  getOfertasActivas(
    ubicacion?: string,
    page = 0,
    size = 10
  ): Observable<any> {
    const params = new URLSearchParams();
    if (ubicacion) params.append('ubicacion', ubicacion);
    params.append('page', page.toString());
    params.append('size', size.toString());

    return this.http.get(
      `${this.apiUrl}?${params.toString()}`
    );
  }
}
