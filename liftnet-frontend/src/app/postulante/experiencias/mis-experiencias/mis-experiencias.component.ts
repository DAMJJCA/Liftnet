import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ExperienciasService } from '../../../core/services/experiencias.service';
import { Experiencia } from '../../../core/services/postulante.service';

@Component({
  selector: 'app-mis-experiencias',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './mis-experiencias.component.html',
  styleUrls: ['./mis-experiencias.component.css']
})
export class MisExperienciasComponent implements OnInit {

  // Signals para reactividad
  experiencias = signal<Experiencia[]>([]);
  loading = signal<boolean>(false);
  enviando = signal<boolean>(false);
  mensajeExito = signal<string | null>(null);
  mensajeError = signal<string | null>(null);

  // Formulario temporal
  nuevaExp: Experiencia = {
    lugar: '',
    descripcion: '',
    fechaInicio: '',
    fechaFin: ''
  };

  constructor(private experienciasSvc: ExperienciasService) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.loading.set(true);
    this.experienciasSvc.getMisExperiencias(0, 50).subscribe({
      next: (res: any) => {
        // Extracción segura a prueba de fallos
        const data = res.data?.content || res.content || res.data || [];
        this.experiencias.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  guardar(): void {
    this.enviando.set(true);
    this.mensajeError.set(null);
    this.mensajeExito.set(null);

    // NOTA: Asegúrate de que el método en tu servicio se llame addExperiencia o createExperiencia
    this.experienciasSvc.addExperiencia(this.nuevaExp).subscribe({
      next: () => {
        this.mensajeExito.set('Experiencia añadida correctamente a tu currículum.');
        this.enviando.set(false);
        // Limpiamos el formulario
        this.nuevaExp = { lugar: '', descripcion: '', fechaInicio: '', fechaFin: '' };
        this.cargar();
        setTimeout(() => this.mensajeExito.set(null), 3000);
      },
      error: () => {
        this.enviando.set(false);
        this.mensajeError.set('No se pudo guardar la experiencia.');
        setTimeout(() => this.mensajeError.set(null), 4000);
      }
    });
  }

  eliminar(id?: any): void {
    if (!id || !confirm('¿Seguro que deseas eliminar este trabajo de tu perfil?')) return;

    this.experienciasSvc.deleteExperiencia(id).subscribe({
      next: () => {
        this.cargar();
      }
    });
  }
}
