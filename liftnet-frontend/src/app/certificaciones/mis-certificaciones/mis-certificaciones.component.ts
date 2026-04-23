import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CertificacionesService } from '../../core/services/certificaciones.service';

@Component({
  selector: 'app-mis-certificaciones',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h2>Mis certificaciones</h2>

    <div *ngIf="loading">Cargando certificaciones...</div>

    <div *ngIf="!loading && certificaciones.length === 0">
      No tienes certificaciones registradas.
    </div>

    <ul *ngIf="!loading">
      <li *ngFor="let cert of certificaciones">
        <strong>{{ cert.nombre }}</strong>
        <span *ngIf="cert.entidad">({{ cert.entidad }})</span>
        <br />
        <small>
          Obtención: {{ cert.fechaObtencion || '—' }} |
          Expiración: {{ cert.fechaExpiracion || '—' }}
        </small>
        <br />
        <button (click)="eliminar(cert.id)">
          Eliminar
        </button>
      </li>
    </ul>

    <div *ngIf="totalPages > 1">
      <button
        (click)="cambiarPagina(page - 1)"
        [disabled]="page === 0"
      >
        Anterior
      </button>

      <span>Página {{ page + 1 }} de {{ totalPages }}</span>

      <button
        (click)="cambiarPagina(page + 1)"
        [disabled]="page + 1 >= totalPages"
      >
        Siguiente
      </button>
    </div>
  `
})
export class MisCertificacionesComponent implements OnInit {

  certificaciones: any[] = [];
  loading = false;

  page = 0;
  size = 5;
  totalPages = 0;

  constructor(
    private certificacionesService: CertificacionesService
  ) {}

  ngOnInit(): void {
    this.cargarCertificaciones();
  }

  cargarCertificaciones(): void {
    this.loading = true;

    this.certificacionesService
      .getMisCertificaciones(this.page, this.size)
      .subscribe({
        next: (response) => {
          this.certificaciones = response.data.content;
          this.totalPages = response.data.totalPages;
          this.loading = false;
        },
        error: () => {
          this.loading = false;
        }
      });
  }

  cambiarPagina(nuevaPagina: number): void {
    this.page = nuevaPagina;
    this.cargarCertificaciones();
  }

  eliminar(id: string): void {
    const confirmado = confirm('¿Eliminar esta certificación?');
    if (!confirmado) {
      return;
    }

    this.certificacionesService.eliminarCertificacion(id).subscribe({
      next: () => {
        this.cargarCertificaciones();
      }
    });
  }
}
