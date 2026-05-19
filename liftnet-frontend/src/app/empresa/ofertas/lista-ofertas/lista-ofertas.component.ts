import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OfertaService, Oferta } from '../../../core/services/oferta.service';
import { PostulacionService } from '../../../core/services/postulacion.service';

@Component({
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lista-ofertas.component.html',
  styleUrls: ['./lista-ofertas.component.css']
})
export class ListaOfertasComponent implements OnInit {

  ofertas: Oferta[] = [];
  mensaje: string | null = null;
  error: string | null = null;

  constructor(
    private ofertaService: OfertaService,
    private postulacionService: PostulacionService
  ) {}

  ngOnInit(): void {
    this.ofertaService.getOfertasActivas().subscribe(res => {
      // Extraemos el array del Pageable de Spring Boot
      this.ofertas = res.data?.content || res.data || [];
    });
  }

  postular(ofertaId: string): void {
    this.mensaje = null;
    this.error = null;

    this.postulacionService.postular(ofertaId).subscribe({
      next: () => {
        this.mensaje = '¡Postulación realizada con éxito!';
      },
      error: err => {
        if (err.status === 409) {
          this.error = 'Ya te has postulado a esta oferta previamente.';
        } else {
          this.error = 'No se pudo completar la postulación. Revisa que tu perfil esté completo.';
        }
      }
    });
  }
}
