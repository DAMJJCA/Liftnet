import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

import { PostulacionService } from '../../core/services/postulacion.service';

@Component({
  standalone: true,
  imports: [CommonModule],
  templateUrl: './empresa-postulaciones.component.html',
  styleUrls: ['./empresa-postulaciones.component.css']
})
export class EmpresaPostulacionesComponent implements OnInit {

  ofertaId!: string;
  postulaciones: any[] = [];
  mensaje: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private postulacionService: PostulacionService
  ) {}

  ngOnInit(): void {
    this.ofertaId = this.route.snapshot.paramMap.get('ofertaId')!;
    this.cargar();
  }

  cargar(): void {
    this.postulacionService
      .getPostulacionesOferta(this.ofertaId)
      .subscribe(res => {
        this.postulaciones = res.data.content;
      });
  }

  cambiarEstado(id: string, estado: 'ACEPTADA' | 'RECHAZADA'): void {
    this.postulacionService.actualizarEstado(id, estado).subscribe(() => {
      this.mensaje = `Postulación ${estado.toLowerCase()}`;
      this.cargar();
    });
  }
}
``
