import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { EmpresaService, EmpresaProfile } from '../../core/services/empresa.service';
import { TokenStorageService } from '../../core/services/token-storage.service';
import { UiStateService } from '../../core/services/ui-state.service';

@Component({
  selector: 'app-empresa-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './empresa-profile.component.html',
  styleUrls: ['./empresa-profile.component.css']
})
export class EmpresaProfileComponent implements OnInit {

  private empresaService = inject(EmpresaService);
  private tokenStorage = inject(TokenStorageService);
  private uiState = inject(UiStateService);
  private router = inject(Router);

  // State
  profile = signal<EmpresaProfile>({ nombreEmpresa: '', ubicacion: '', telefono: '', descripcion: '' });
  mode = signal<'view' | 'edit'>('view');
  loading = signal(false);
  errorMessage = signal<string | null>(null);
  successMessage = signal<string | null>(null);
  mandatoryMessage: string | null = null;

  // Calculamos el progreso de la empresa (cada campo vale 25%)
  progress = computed(() => {
    const p = this.profile();
    let score = 0;
    if (p.nombreEmpresa && p.nombreEmpresa.trim() !== '') score += 25;
    if (p.ubicacion && p.ubicacion.trim() !== '') score += 25;
    if (p.telefono && p.telefono.trim() !== '') score += 25;
    if (p.descripcion && p.descripcion.trim() !== '') score += 25;
    return score;
  });

  ngOnInit(): void {
    this.mandatoryMessage = this.uiState.getProfileMessage();
    this.uiState.clearProfileMessage();
    this.cargarPerfil();
  }

  cargarPerfil(): void {
    this.loading.set(true);
    this.empresaService.getProfile().subscribe({
      next: (res) => {
        if (res.data) {
          this.profile.set(res.data);
          this.mode.set('view');
        } else {
          this.mode.set('edit');
        }
        this.loading.set(false);
      },
      error: () => {
        this.mode.set('edit');
        this.loading.set(false);
      }
    });
  }

  activarEdicion(): void {
    this.mode.set('edit');
  }

  cancelarEdicion(): void {
    this.mode.set('view');
  }

  saveProfile(): void {
    this.loading.set(true);
    this.errorMessage.set(null);
    this.successMessage.set(null);

    const isNew = !this.tokenStorage.isProfileCompleted();
    const request$ = isNew
      ? this.empresaService.createProfile(this.profile())
      : this.empresaService.updateProfile(this.profile());

    request$.subscribe({
      next: (res) => {
        if (res.data) {
          this.profile.set(res.data);
        }
        this.mode.set('view');
        this.tokenStorage.saveProfileCompleted(true);
        this.successMessage.set('Perfil de empresa actualizado con éxito.');

        setTimeout(() => this.successMessage.set(null), 3000);

        if (isNew) {
          this.router.navigate(['/empresa/ofertas']);
        } else {
          this.loading.set(false);
        }
      },
      error: () => {
        this.errorMessage.set('Error al guardar el perfil de la empresa.');
        this.loading.set(false);
      }
    });
  }
}
