import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PostulacionService, Postulacion } from '../../core/services/postulacion.service';

@Component({
  selector: 'app-postulante-postulaciones',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './postulante-postulaciones.component.html',
  styleUrls: ['./postulante-postulaciones.component.css']
})
export class PostulacionesComponent implements OnInit {

  postulaciones = signal<Postulacion[]>([]);
  loading = signal<boolean>(true);
  errorMessage = signal<string | null>(null);

  constructor(private postulacionService: PostulacionService) {}

  ngOnInit(): void {
    this.cargarPostulaciones();
  }

  cargarPostulaciones(): void {
    this.loading.set(true);
    this.errorMessage.set(null);
    this.postulacionService.getMisPostulaciones().subscribe({
      next: (res: any) => {
        // Extraemos el array paginado del backend
        const arrayData = res.data?.content || res.content || res.data || [];
        this.postulaciones.set(arrayData);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error fetching postulaciones:', err);
        this.errorMessage.set('No se pudieron cargar tus postulaciones. Intentalo de nuevo en unos segundos.');
        this.loading.set(false);
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

  retirar(postulacionId: string): void {
    if (!confirm('¿Seguro que quieres retirar tu candidatura? Esta acción no se puede deshacer.')) return;

    this.postulacionService.retirarPostulacion(postulacionId).subscribe({
      next: () => {
        this.postulaciones.update(list => list.filter(p => p.id !== postulacionId));
      },
      error: (err) => {
        const msg = err.status === 400
          ? 'Solo puedes retirar candidaturas en estado pendiente.'
          : 'No se pudo retirar la candidatura. Inténtalo de nuevo.';
        this.errorMessage.set(msg);
      }
    });
  }

  estadoDescripcion(estado: string | null | undefined): string {
    switch (estado) {
      case 'ACEPTADA':
        return 'La empresa ha aceptado tu candidatura. Revisa los datos de contacto para continuar.';
      case 'RECHAZADA':
        return 'La empresa ha descartado esta candidatura. Puedes seguir buscando otras ofertas.';
      case 'PENDIENTE':
        return 'La empresa todavia no ha revisado tu candidatura.';
      default:
        return 'Estado pendiente de confirmar.';
    }
  }
}
