import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CertificacionesService, CertificacionPostulante } from '../../../core/services/certificaciones.service';
import { SupabaseService } from '../../../core/services/supabase.service'; // <--- Importado

@Component({
  selector: 'app-mis-certificaciones',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './mis-certificaciones.component.html',
  styleUrls: ['./mis-certificaciones.component.css']
})
export class MisCertificacionesComponent implements OnInit {

  // Signals
  certificaciones = signal<CertificacionPostulante[]>([]);
  loading = signal<boolean>(false);
  enviando = signal<boolean>(false);
  mensajeExito = signal<string | null>(null);
  mensajeError = signal<string | null>(null);

  private supabaseService = inject(SupabaseService);

  // Formulario temporal
  nuevaCert = {
    certificacionId: '',
    fechaObtencion: '',
    fechaExpiracion: '',
    archivoUrl: ''
  };

  selectedFile: File | null = null; // Archivo PDF en espera

  constructor(private certificacionesService: CertificacionesService) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.loading.set(true);
    this.certificacionesService.getMisCertificaciones(0, 50).subscribe({
      next: (res) => {
        const data = res.data?.content || [];
        this.certificaciones.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      if (file.type === 'application/pdf') {
        this.selectedFile = file;
      } else {
        alert('Por favor, selecciona un archivo PDF válido.');
        event.target.value = ''; // Resetea el input
        this.selectedFile = null;
      }
    }
  }

  async asignar() {
    this.enviando.set(true);
    this.mensajeError.set(null);
    this.mensajeExito.set(null);

    try {
      // 1. Si hay archivo, lo subimos primero a Supabase
      if (this.selectedFile) {
        // Usamos el método uploadFile que te pasé en el mensaje anterior
        const url = await this.supabaseService.uploadDocument(this.selectedFile, 'certificados');
        this.nuevaCert.archivoUrl = url;
      }

      // 2. Mandamos todo a Spring Boot
      this.certificacionesService.asignarCertificacion(this.nuevaCert).subscribe({
        next: () => {
          this.mensajeExito.set('Certificación añadida a tu perfil.');
          this.nuevaCert = { certificacionId: '', fechaObtencion: '', fechaExpiracion: '', archivoUrl: '' };
          this.selectedFile = null;
          this.cargar();
          setTimeout(() => this.mensajeExito.set(null), 3000);
        },
        error: (err) => {
          if (err.error?.message?.includes('posee')) {
            this.mensajeError.set('Ya tienes esta certificación registrada.');
          } else {
            this.mensajeError.set('Error al añadir la certificación.');
          }
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

  async eliminar(cert: CertificacionPostulante) {
    if (!confirm('¿Seguro que deseas eliminar este título de tu perfil?')) return;

    // 1. Borramos de Supabase si tenía PDF para no acumular basura
    if (cert.archivoUrl) {
      await this.supabaseService.deleteDocument(cert.archivoUrl, 'certificados');
    }

    // 2. Borramos de la Base de Datos
    this.certificacionesService.eliminarCertificacion(cert.id).subscribe({
      next: () => {
        this.cargar();
      }
    });
  }
}
