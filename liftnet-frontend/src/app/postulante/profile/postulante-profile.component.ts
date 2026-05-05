import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { UiStateService } from '../../core/services/ui-state.service';
import { PostulanteProfileStore } from '../../core/stores/postulante-profile.store';

@Component({
  selector: 'app-postulante-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './postulante-profile.component.html',
  styleUrls: ['./postulante-profile.component.css']
})
export class PostulanteProfileComponent {

  public store = inject(PostulanteProfileStore); // Lo hacemos público para el HTML
  private uiState = inject(UiStateService);

  mandatoryMessage: string | null = null;

  constructor() {
    this.mandatoryMessage = this.uiState.getProfileMessage();
    this.uiState.clearProfileMessage();
    this.store.cargarTodo(); // Cargamos todo al iniciar
  }

  activarEdicion(): void {
    this.store.mode.set('edit');
  }

  cancelarEdicion(): void {
    this.store.mode.set('view');
  }

  saveProfile(): void {
    this.store.guardarDatosBasicos();
  }
}
