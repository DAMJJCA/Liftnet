import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ExperienciasService } from '../../../core/services/experiencias.service';
import { Experiencia } from '../../../core/services/postulante.service';
import { SupabaseService } from '../../../core/services/supabase.service'; // <-- Importante

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

  private supabaseService = inject(SupabaseService);

  // Formulario temporal
  nuevaExp: Experiencia = {
    lugar: '',
    descripcion: '',
    fechaInicio: '',
    fechaFin: '',
    archivoUrl: '' // <-- Añadido
  };

  selectedFile: File | null = null; // Para guardar el PDF en memoria

  constructor(private experienciasSvc: ExperienciasService) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.loading.set(true);
    this.experienciasSvc.getMisExperiencias(0, 50).subscribe({
      next: (res: any) => {
        const data = res.data?.content || res.content || res.data || [];
        this.experiencias.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  // --- NUEVO: Selección de archivo ---
  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      if (file.type === 'application/pdf') {
        this.selectedFile = file;
      } else {
        alert('Por favor, selecciona un archivo PDF válido.');
        event.target.value = '';
        this.selectedFile = null;
      }
    }
  }

  async guardar() {
    this.enviando.set(true);
    this.mensajeError.set(null);
    this.mensajeExito.set(null);

    try {
      // 1. Si hay un archivo, lo subimos al bucket 'experiencia'
      if (this.selectedFile) {
        const url = await this.supabaseService.uploadDocument(this.selectedFile, 'experiencia');
        this.nuevaExp.archivoUrl = url;
      }

      // 2. Mandamos al backend
      this.experienciasSvc.addExperiencia(this.nuevaExp).subscribe({
        next: () => {
          this.mensajeExito.set('Experiencia añadida correctamente a tu currículum.');
          // Limpiamos
          this.nuevaExp = { lugar: '', descripcion: '', fechaInicio: '', fechaFin: '', archivoUrl: '' };
          this.selectedFile = null;
          this.cargar();
          setTimeout(() => this.mensajeExito.set(null), 3000);
        },
        error: () => {
          this.mensajeError.set('No se pudo guardar la experiencia.');
          setTimeout(() => this.mensajeError.set(null), 4000);
        }
      });
    } catch (error) {
       this.mensajeError.set('Error al subir el documento PDF a la nube.');
       setTimeout(() => this.mensajeError.set(null), 4000);
    } finally {
       this.enviando.set(false);
    }
  }

  async eliminar(exp: Experiencia) {
    if (!confirm('¿Seguro que deseas eliminar este trabajo de tu perfil?')) return;

    // 1. Borramos el archivo de Supabase si existe
    if (exp.archivoUrl) {
       await this.supabaseService.deleteDocument(exp.archivoUrl, 'experiencia');
    }

    // 2. Borramos de la Base de Datos (pasamos el ID)
    this.experienciasSvc.deleteExperiencia(exp.id).subscribe({
      next: () => {
        this.cargar();
      }
    });
  }
}
