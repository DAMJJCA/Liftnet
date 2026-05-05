import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CertificacionesService, CertificacionPostulante } from '../../../core/services/certificaciones.service';

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

  // Formulario temporal
  nuevaCert = {
    certificacionId: '',
    fechaObtencion: '',
    fechaExpiracion: ''
  };

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

  asignar(): void {
    this.enviando.set(true);
    this.mensajeError.set(null);
    this.mensajeExito.set(null);

    this.certificacionesService.asignarCertificacion(this.nuevaCert).subscribe({
      next: () => {
        this.mensajeExito.set('Certificación añadida a tu perfil.');
        this.enviando.set(false);
        this.nuevaCert = { certificacionId: '', fechaObtencion: '', fechaExpiracion: '' }; // Limpiar
        this.cargar();
        setTimeout(() => this.mensajeExito.set(null), 3000);
      },
      error: (err) => {
        this.enviando.set(false);
        if (err.error?.message?.includes('posee')) {
          this.mensajeError.set('Ya tienes esta certificación registrada.');
        } else {
          this.mensajeError.set('Error al añadir la certificación.');
        }
        setTimeout(() => this.mensajeError.set(null), 4000);
      }
    });
  }

  eliminar(id: string): void {
    if (!confirm('¿Seguro que deseas eliminar este título de tu perfil?')) return;

    this.certificacionesService.eliminarCertificacion(id).subscribe({
      next: () => {
        this.cargar();
      }
    });
  }
}
