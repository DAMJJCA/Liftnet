import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
  PostulacionService,
  Postulacion
} from '../../core/services/postulacion.service';

@Component({
  selector: 'app-postulante-postulaciones',
  standalone: true,
  imports: [CommonModule],
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
        console.log('Postulaciones del postulante:', res);

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
