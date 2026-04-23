import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ExperienciasService } from '../../core/services/experiencias.service';

@Component({
  selector: 'app-mis-experiencias',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h2>Mis experiencias</h2>

    <div *ngIf="loading">Cargando experiencias...</div>

    <div *ngIf="!loading && experiencias.length === 0">
      No has registrado experiencias todavía.
    </div>

    <ul *ngIf="!loading">
      <li *ngFor="let exp of experiencias">
        <strong>{{ exp.lugar }}</strong>
        <br />

        <small *ngIf="exp.descripcion">
          {{ exp.descripcion }}
        </small>
        <br />

        <small>
          Inicio: {{ exp.fechaInicio || '—' }} |
          Fin: {{ exp.fechaFin || '—' }}
        </small>
        <br />

        <button (click)="eliminar(exp.id)">
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

      <span>
        Página {{ page + 1 }} de {{ totalPages }}
      </span>

      <button
        (click)="cambiarPagina(page + 1)"
        [disabled]="page + 1 >= totalPages"
      >
        Siguiente
      </button>
    </div>
  `
})
export class MisExperienciasComponent implements OnInit {

  experiencias: any[] = [];
  loading = false;

  page = 0;
  size = 5;
  totalPages = 0;

  constructor(
    private experienciasService: ExperienciasService
  ) {}

  ngOnInit(): void {
    this.cargarExperiencias();
  }

  cargarExperiencias(): void {
    this.loading = true;

    this.experienciasService
      .getMisExperiencias(this.page, this.size)
      .subscribe({
        next: (response) => {
          this.experiencias = response.data.content;
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
    this.cargarExperiencias();
  }

  eliminar(id: string): void {
    const confirmado = confirm('¿Eliminar esta experiencia?');
    if (!confirmado) {
      return;
    }

    this.experienciasService.eliminarExperiencia(id).subscribe({
      next: () => {
        this.cargarExperiencias();
      }
    });
  }
}
