import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { HomeStore } from '../core/stores/home.store';
import { TokenStorageService } from '../core/services/token-storage.service';
import { PostulacionService } from '../core/services/postulacion.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  private store = inject(HomeStore);
  private tokenStorage = inject(TokenStorageService);
  private postulacionService = inject(PostulacionService);

  ofertas = this.store.ofertas;
  loading = this.store.loading;
  error = this.store.error;

  // NUEVO: Guardaremos los IDs de las ofertas a las que ya aplicó
  ofertasPostuladas = signal<string[]>([]);

  mensajeExito: string | null = null;
  mensajeError: string | null = null;
  procesandoId: string | null = null;

  constructor() {
    this.store.cargarOfertas();
  }

  ngOnInit(): void {
    // Si es un postulante, cargamos sus postulaciones previas para bloquear los botones
    if (this.isLoggedIn() && this.isPostulante()) {
      this.cargarPostulacionesPrevias();
    }
  }

  cargarPostulacionesPrevias(): void {
    // Pedimos hasta 100 postulaciones para mapear los IDs
    this.postulacionService.getMisPostulaciones(0, 100).subscribe({
      next: (res: any) => {
        const arrayData = res.data?.content || res.content || res.data || [];
        const idsPostuladas = arrayData.map((p: any) => p.ofertaId);
        this.ofertasPostuladas.set(idsPostuladas);
      }
    });
  }

  isLoggedIn(): boolean {
    return this.tokenStorage.isLoggedIn();
  }

  isPostulante(): boolean {
    return this.tokenStorage.currentRole() === 'POSTULANTE';
  }

  postular(ofertaId: string): void {
    this.mensajeExito = null;
    this.mensajeError = null;
    this.procesandoId = ofertaId;

    this.postulacionService.postular(ofertaId).subscribe({
      next: () => {
        this.mensajeExito = '¡Te has postulado con éxito a la oferta!';
        this.procesandoId = null;
        // Añadimos la oferta a la lista local para desactivar el botón inmediatamente
        this.ofertasPostuladas.update(ids => [...ids, ofertaId]);
        setTimeout(() => this.mensajeExito = null, 4000);
      },
      error: (err) => {
        this.procesandoId = null; // Liberamos el botón

        // Extraemos el mensaje del backend
        const msg = err.error?.message || '';

        if (msg.includes('Already applied') || msg.includes('ya')) {
          this.mensajeError = 'Ya te habías postulado a esta oferta anteriormente.';
          // Bloqueamos el botón localmente por si acaso
          this.ofertasPostuladas.update(ids => [...ids, ofertaId]);
        } else {
          this.mensajeError = 'No se pudo procesar la postulación.';
        }

        setTimeout(() => this.mensajeError = null, 4000);
      }
    });
  }
}
