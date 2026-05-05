import { Injectable, signal, inject } from '@angular/core';
import { OfertaService, Oferta } from '../services/oferta.service';

@Injectable({ providedIn: 'root' })
export class HomeStore {

  private ofertaService = inject(OfertaService);

  ofertas = signal<Oferta[]>([]);
  loading = signal<boolean>(false);
  error = signal<string | null>(null);

  cargarOfertas(): void {
    this.loading.set(true);
    this.error.set(null);

    this.ofertaService.getOfertasActivas().subscribe({
      next: (res) => {
        // Manejamos la estructura ApiResponse + Page de Spring
        const content = res.data?.content || res.content || [];
        this.ofertas.set(content);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('No se pudieron cargar las ofertas en este momento.');
        this.loading.set(false);
      }
    });
  }
}
