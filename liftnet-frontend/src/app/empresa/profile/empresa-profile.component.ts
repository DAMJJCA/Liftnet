import { Component, OnInit, inject } from '@angular/core';
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
  private router = inject(Router);
  private uiState = inject(UiStateService);

  profile: EmpresaProfile = {
    nombreEmpresa: '',
    ubicacion: '',
    telefono: '',
    descripcion: ''
  };

  loading = true;
  errorMessage: string | null = null;
  successMessage: string | null = null;
  mandatoryMessage: string | null = null;

  ngOnInit(): void {
    this.mandatoryMessage = this.uiState.getProfileMessage();
    this.uiState.clearProfileMessage();
    this.loadProfile();
  }

  loadProfile(): void {
    this.empresaService.getProfile().subscribe({
      next: res => {
        if (res.data) this.profile = res.data;
        this.loading = false;
      },
      error: () => {
        // Perfil aún no existe, dejamos el form vacío
        this.loading = false;
      }
    });
  }

  save(): void {
    this.successMessage = null;
    this.errorMessage = null;

    const isNew = !this.tokenStorage.isProfileCompleted();

    const request$ = isNew
      ? this.empresaService.createProfile(this.profile)
      : this.empresaService.updateProfile(this.profile);

    request$.subscribe({
      next: () => {
        // ✅ Marcar perfil como completo
        this.tokenStorage.saveProfileCompleted(true);

        if (isNew) {
          // Primera vez → ir al dashboard
          this.router.navigate(['/ofertas/mis-ofertas']);
        } else {
          this.successMessage = 'Perfil actualizado correctamente';
        }
      },
      error: () => {
        this.errorMessage = 'No se pudo guardar el perfil';
      }
    });
  }
}
