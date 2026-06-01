import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

import { PostulacionService } from '../../core/services/postulacion.service';

@Component({
  selector: 'app-empresa-postulaciones',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './empresa-postulaciones.component.html',
  styleUrls: ['./empresa-postulaciones.component.css']
})
export class EmpresaPostulacionesComponent implements OnInit {

  // Signals para reactividad instantánea
  ofertaId = signal<string>('');
  postulaciones = signal<any[]>([]);
  mensaje = signal<string | null>(null);
  errorMessage = signal<string | null>(null);
  loading = signal<boolean>(true);
  procesandoId = signal<string | null>(null);

  constructor(
    private route: ActivatedRoute,
    private postulacionService: PostulacionService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('ofertaId')!;
    this.ofertaId.set(id);
    this.cargar();
  }

  cargar(): void {
    this.loading.set(true);
    this.errorMessage.set(null);
    this.postulacionService.getPostulacionesOferta(this.ofertaId()).subscribe({
      next: (res) => {
        // Extraemos el array y lo metemos en la Signal
        const arrayData = res.data?.content || res.content || res.data || [];
        this.postulaciones.set(arrayData);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error cargando postulaciones:', err);
        this.errorMessage.set('No se pudieron cargar los candidatos. Intentalo de nuevo en unos segundos.');
        this.loading.set(false);
      }
    });
  }

  cambiarEstado(id: string, estado: 'ACEPTADA' | 'RECHAZADA'): void {
    if (this.procesandoId()) {
      return;
    }

    this.errorMessage.set(null);
    this.mensaje.set(null);
    this.procesandoId.set(id);

    this.postulacionService.actualizarEstado(id, estado).subscribe({
      next: () => {
        this.mensaje.set(`Candidato ${this.estadoTexto(estado).toLowerCase()} correctamente.`);
        this.procesandoId.set(null);
        this.cargar();
        setTimeout(() => this.mensaje.set(null), 4000);
      },
      error: (err) => {
        console.error('Error actualizando estado:', err);
        this.errorMessage.set('No se pudo actualizar el estado del candidato. Intentalo de nuevo.');
        this.procesandoId.set(null);
      }
    });
  }

  estadoClass(estado: string | null | undefined): string {
    return (estado || 'pendiente').toLowerCase();
  }

  estadoTexto(estado: string | null | undefined): string {
    switch (estado) {
      case 'ACEPTADA':
        return 'Aceptada';
      case 'RECHAZADA':
        return 'Rechazada';
      case 'PENDIENTE':
        return 'Pendiente';
      default:
        return 'Pendiente';
    }
  }

  estaProcesando(id: string): boolean {
    return this.procesandoId() === id;
  }
}
