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
  loading = signal<boolean>(true);

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
    this.postulacionService.getPostulacionesOferta(this.ofertaId()).subscribe({
      next: (res) => {
        // Extraemos el array y lo metemos en la Signal
        const arrayData = res.data?.content || res.content || res.data || [];
        this.postulaciones.set(arrayData);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error cargando postulaciones:', err);
        this.loading.set(false);
      }
    });
  }

  cambiarEstado(id: string, estado: 'ACEPTADA' | 'RECHAZADA'): void {
    this.postulacionService.actualizarEstado(id, estado).subscribe(() => {
      this.mensaje.set(`¡Candidato ${estado.toLowerCase()} correctamente!`);
      this.cargar();
      setTimeout(() => this.mensaje.set(null), 4000);
    });
  }
}
