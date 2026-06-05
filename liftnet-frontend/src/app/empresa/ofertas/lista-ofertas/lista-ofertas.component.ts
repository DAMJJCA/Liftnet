import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { retry, timeout } from 'rxjs';

import { OfertaService, Oferta } from '../../../core/services/oferta.service';
import { Postulacion, PostulacionService } from '../../../core/services/postulacion.service';
import { TokenStorageService } from '../../../core/services/token-storage.service';

interface PostulacionInfo {
  postulacionId: string;
  estado: string;
}

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './lista-ofertas.component.html',
  styleUrls: ['./lista-ofertas.component.css']
})
export class ListaOfertasComponent implements OnInit {

  // --- Estado local como signals ---
  ofertas    = signal<Oferta[]>([]);
  postuladas = signal<Map<string, PostulacionInfo>>(new Map());
  mensaje    = signal<string | null>(null);
  error      = signal<string | null>(null);
  loading    = signal(true);
  showPerfilModal = signal(false);

  // --- Filtros ---
  // filtroUbicacion: se envía al backend en cada búsqueda (GET /api/v1/ofertas?ubicacion=...)
  // filtroNivel: se aplica en memoria sobre las ofertas ya cargadas (campo string libre)
  filtroUbicacion = '';
  filtroNivel     = '';

  // Ofertas tras aplicar el filtro de nivel en cliente
  get ofertasFiltradas(): Oferta[] {
    const nivel = this.filtroNivel.trim().toLowerCase();
    if (!nivel) return this.ofertas();
    return this.ofertas().filter(o =>
      o.nivel?.toLowerCase().includes(nivel)
    );
  }

  get perfilCompleto(): boolean {
    return this.tokenStorage.isProfileCompleted();
  }

  // takeUntilDestroyed solo para getMisPostulaciones (la petición secundaria sí puede cancelarse)
  private readonly destroyRef = inject(DestroyRef);

  constructor(
    private ofertaService: OfertaService,
    private postulacionService: PostulacionService,
    private tokenStorage: TokenStorageService
  ) {}

  ngOnInit(): void {
    this.cargarOfertas();
  }

  cargarOfertas(ubicacion?: string): void {
    this.error.set(null);
    this.loading.set(true);
    this.ofertas.set([]);

    // Sin takeUntilDestroyed en getOfertasActivas: la petición huérfana calienta
    // la conexión HikariCP/Supabase para la siguiente visita a /ofertas.
    this.ofertaService.getOfertasActivas(ubicacion).pipe(
      timeout(8000),
      retry({ count: 2, delay: 800 })
    ).subscribe({
      next: (res) => {
        this.ofertas.set(this.extraerContenido<Oferta>(res));
        this.loading.set(false);
      },
      error: () => {
        this.error.set('No se pudieron cargar las ofertas. Inténtalo de nuevo.');
        this.loading.set(false);
      }
    });

    this.recargarPostulaciones();
  }

  buscar(): void {
    this.mensaje.set(null);
    this.cargarOfertas(this.filtroUbicacion.trim() || undefined);
  }

  limpiarFiltros(): void {
    this.filtroUbicacion = '';
    this.filtroNivel = '';
    this.cargarOfertas();
  }

  postular(ofertaId: string): void {
    this.mensaje.set(null);
    this.error.set(null);

    if (!this.perfilCompleto) {
      this.showPerfilModal.set(true);
      return;
    }

    this.postulacionService.postular(ofertaId).subscribe({
      next: () => {
        this.mensaje.set('¡Postulación realizada con éxito!');
        this.recargarPostulaciones();
      },
      error: err => {
        if (err.status === 409) {
          this.error.set('Ya te has postulado a esta oferta previamente.');
          this.recargarPostulaciones();
        } else {
          this.error.set('No se pudo completar la postulación. Revisa que tu perfil esté completo.');
        }
      }
    });
  }

  retirar(ofertaId: string): void {
    const info = this.postuladas().get(ofertaId);
    if (!info || info.estado !== 'PENDIENTE') return;

    if (!confirm('¿Seguro que quieres retirar tu candidatura? Esta acción no se puede deshacer.')) return;

    this.mensaje.set(null);
    this.error.set(null);

    this.postulacionService.retirarPostulacion(info.postulacionId).subscribe({
      next: () => {
        const nuevo = new Map(this.postuladas());
        nuevo.delete(ofertaId);
        this.postuladas.set(nuevo); // signal notifica directamente
        this.mensaje.set('Candidatura retirada correctamente.');
      },
      error: err => {
        this.error.set(
          err.status === 400
            ? 'Solo puedes retirar candidaturas en estado pendiente.'
            : 'No se pudo retirar la candidatura. Inténtalo de nuevo.'
        );
      }
    });
  }

  cerrarModal(): void {
    this.showPerfilModal.set(false);
  }

  private recargarPostulaciones(): void {
    this.postulacionService.getMisPostulaciones(0, 100).pipe(
      timeout(8000),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: (res) => {
        const map = new Map<string, PostulacionInfo>();
        this.extraerContenido<Postulacion>(res).forEach(p => {
          if (p.ofertaId) {
            map.set(p.ofertaId, { postulacionId: p.id, estado: p.estado });
          }
        });
        this.postuladas.set(map);
      },
      error: () => { /* fallo silencioso */ }
    });
  }

  private extraerContenido<T>(res: any): T[] {
    const data = res?.data?.content || res?.content || res?.data || [];
    return Array.isArray(data) ? data : [];
  }
}
