import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
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
  imports: [CommonModule, RouterModule],
  templateUrl: './lista-ofertas.component.html',
  styleUrls: ['./lista-ofertas.component.css']
})
export class ListaOfertasComponent implements OnInit {

  // --- Estado local como signals ---
  // Ventaja sobre class properties: notifican al sistema reactivo de Angular directamente,
  // sin depender de zone.js para detectar cambios. Elimina el hack "new Map(this.postuladas)"
  // que antes era necesario para forzar detección de cambio en el Map.
  ofertas    = signal<Oferta[]>([]);
  postuladas = signal<Map<string, PostulacionInfo>>(new Map());
  mensaje    = signal<string | null>(null);
  error      = signal<string | null>(null);
  loading    = signal(true);
  showPerfilModal = signal(false);

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

  cargarOfertas(): void {
    this.error.set(null);
    this.loading.set(true);
    this.ofertas.set([]);

    // Sin takeUntilDestroyed en getOfertasActivas:
    // La petición huérfana (si el usuario navega fuera) sigue en background
    // y calienta la conexión HikariCP/Supabase para la siguiente visita.
    this.ofertaService.getOfertasActivas().pipe(
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
        this.postuladas.set(nuevo); // signal notifica directamente, sin hack de referencia
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
