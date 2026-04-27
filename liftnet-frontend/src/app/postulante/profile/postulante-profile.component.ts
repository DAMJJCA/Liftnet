import { Component, inject, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { UiStateService } from '../../core/services/ui-state.service';
import { PostulanteProfileStore } from '../../core/stores/postulante-profile.store';

@Component({
  selector: 'app-postulante-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './postulante-profile.component.html',
  styleUrls: ['./postulante-profile.component.css']
})
export class PostulanteProfileComponent {

  private store = inject(PostulanteProfileStore);
  private uiState = inject(UiStateService);

  // Estado base
  profile = this.store.profile;
  loading = this.store.loading;
  errorMessage = this.store.errorMessage;
  successMessage = this.store.successMessage;

  mandatoryMessage: string | null = null;

  // =========================
  // SECCIONES DEL PERFIL
  // =========================
  sections = computed(() => {
    const p = this.profile();

    return {
      basicInfo: !!(p.nombre && p.email),
      experiences: Array.isArray(p.experiencias) && p.experiencias.length > 0,
      certifications: Array.isArray(p.certificaciones) && p.certificaciones.length > 0,
    };
  });

  // =========================
  // CAPACIDADES (LinkedIn-like)
  // =========================
  canApply = computed(() => this.sections().certifications);

  progress = computed(() => {
    const values = Object.values(this.sections());
    const completed = values.filter(v => v).length;
    return Math.round((completed / values.length) * 100);
  });

  constructor() {
    this.mandatoryMessage = this.uiState.getProfileMessage();
    this.uiState.clearProfileMessage();
    this.store.cargarPerfil();
  }

  saveProfile(): void {
    this.store.guardarPerfil();
  }
}
