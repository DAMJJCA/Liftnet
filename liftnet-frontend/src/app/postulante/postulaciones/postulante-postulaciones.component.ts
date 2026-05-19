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

  constructor(private postulacionService: PostulacionService) {}

  ngOnInit(): void {
    this.cargarPostulaciones();
  }

  cargarPostulaciones(): void {
    this.loading.set(true);
    this.postulacionService.getMisPostulaciones().subscribe({
      next: (res: any) => {
        // Extraemos el array paginado del backend
        const arrayData = res.data?.content || res.content || res.data || [];
        this.postulaciones.set(arrayData);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error fetching postulaciones:', err);
        this.loading.set(false);
      }
    });
  }
}
