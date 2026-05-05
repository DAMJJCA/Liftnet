import { Injectable, signal, inject } from '@angular/core';
import { OfertaService, Oferta } from '../services/oferta.service';

@Injectable({ providedIn: 'root' })
export class EmpresaOfertasStore {

  private ofertaService = inject(OfertaService);

  // Estado reactivo (Signals)
  ofertas = signal<Oferta[]>([]);
  loading = signal<boolean>(false);
  error = signal<string | null>(null);
  successMessage = signal<string | null>(null);

  // CARGAR OFERTAS
  cargarOfertas(page = 0, size = 20): void {
    this.loading.set(true);
    this.error.set(null);

    this.ofertaService.getMisOfertas(page, size).subscribe({
      next: (res) => {
        // Spring Data REST paginado devuelve 'content' dentro de 'data' o directamente en la raíz
        const content = res.data?.content || res.content || [];
        this.ofertas.set(content);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('No se pudieron cargar tus ofertas');
        this.loading.set(false);
      }
    });
  }

  // CREAR OFERTA
  crearOferta(nuevaOferta: { titulo: string; descripcion: string; ubicacion: string }): void {
    this.loading.set(true);
    this.ofertaService.crearOferta(nuevaOferta).subscribe({
      next: () => {
        this.successMessage.set('Oferta publicada con éxito');
        this.cargarOfertas(); // Recargamos la lista
        setTimeout(() => this.successMessage.set(null), 3000);
      },
      error: () => {
        this.error.set('Error al publicar la oferta');
        this.loading.set(false);
      }
    });
  }

  // EDITAR OFERTA
  editarOferta(id: string, oferta: { titulo: string; descripcion: string; ubicacion: string }): void {
    this.loading.set(true);
    this.ofertaService.editarOferta(id, oferta).subscribe({
      next: () => {
        this.successMessage.set('Oferta actualizada');
        this.cargarOfertas();
        setTimeout(() => this.successMessage.set(null), 3000);
      },
      error: () => {
        this.error.set('Error al editar la oferta');
        this.loading.set(false);
      }
    });
  }

  // CERRAR OFERTA
  cerrarOferta(id: string): void {
    if (!confirm('¿Estás seguro de que quieres cerrar esta oferta? Ya no se recibirán más postulantes.')) return;

    this.loading.set(true);
    this.ofertaService.cerrarOferta(id).subscribe({
      next: () => {
        this.successMessage.set('Oferta cerrada');
        this.cargarOfertas();
        setTimeout(() => this.successMessage.set(null), 3000);
      },
      error: () => {
        this.error.set('Error al cerrar la oferta');
        this.loading.set(false);
      }
    });
  }
}
