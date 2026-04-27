import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OfertaService, Oferta } from '../../../core/services/oferta.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-mis-ofertas',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './mis-ofertas.component.html',
  styleUrls: ['./mis-ofertas.component.css']
})
export class MisOfertasComponent implements OnInit {

  ofertas: Oferta[] = [];

  nuevaOferta = {
    titulo: '',
    descripcion: '',
    ubicacion: ''
  };

  ofertaEditando: Oferta | null = null;

  constructor(private ofertaService: OfertaService) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.ofertaService.getMisOfertas().subscribe(res => {
      this.ofertas = res.content;
    });
  }

  crear(): void {
    this.ofertaService.crearOferta(this.nuevaOferta).subscribe(() => {
      this.nuevaOferta = { titulo: '', descripcion: '', ubicacion: '' };
      this.cargar();
    });
  }

  editar(oferta: Oferta): void {
    this.ofertaEditando = { ...oferta };
  }

  cancelarEdicion(): void {
    this.ofertaEditando = null;
  }

  guardarEdicion(): void {
    if (!this.ofertaEditando) return;

    this.ofertaService
      .editarOferta(this.ofertaEditando.id, {
        titulo: this.ofertaEditando.titulo,
        descripcion: this.ofertaEditando.descripcion,
        ubicacion: this.ofertaEditando.ubicacion
      })
      .subscribe(() => {
        this.ofertaEditando = null;
        this.cargar();
      });
  }

  cerrar(ofertaId: string): void {
    this.ofertaService.cerrarOferta(ofertaId).subscribe(() => {
      this.cargar();
    });
  }
}
