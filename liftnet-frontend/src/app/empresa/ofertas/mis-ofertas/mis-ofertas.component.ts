import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { Oferta } from '../../../core/services/oferta.service';
import { EmpresaOfertasStore } from '../../../core/stores/empresa-ofertas.store';

@Component({
  selector: 'app-mis-ofertas',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './mis-ofertas.component.html',
  styleUrls: ['./mis-ofertas.component.css']
})
export class MisOfertasComponent implements OnInit {

  // Conectamos con el Store
  private store = inject(EmpresaOfertasStore);

  // Signals expuestos a la vista
  ofertas = this.store.ofertas;
  loading = this.store.loading;
  error = this.store.error;
  successMessage = this.store.successMessage;

  // Estado local para los formularios
  nuevaOferta = { titulo: '', descripcion: '', ubicacion: '' };
  ofertaEditando: Oferta | null = null;

  ngOnInit(): void {
    this.store.cargarOfertas();
  }

  crear(): void {
    this.store.crearOferta(this.nuevaOferta);
    // Limpiamos el formulario local
    this.nuevaOferta = { titulo: '', descripcion: '', ubicacion: '' };
  }

  editar(oferta: Oferta): void {
    this.ofertaEditando = { ...oferta };
  }

  cancelarEdicion(): void {
    this.ofertaEditando = null;
  }

  guardarEdicion(): void {
    if (!this.ofertaEditando) return;

    this.store.editarOferta(this.ofertaEditando.id, {
      titulo: this.ofertaEditando.titulo,
      descripcion: this.ofertaEditando.descripcion,
      ubicacion: this.ofertaEditando.ubicacion
    });
    this.ofertaEditando = null;
  }

  cerrar(ofertaId: string): void {
    this.store.cerrarOferta(ofertaId);
  }
}
