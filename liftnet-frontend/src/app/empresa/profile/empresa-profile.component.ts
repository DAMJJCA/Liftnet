import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SupabaseService } from '../../core/services/supabase.service';
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
  private supabaseService = inject(SupabaseService);
  private tokenStorage = inject(TokenStorageService);
  private uiState = inject(UiStateService);
  private router = inject(Router);

  // State
  profile = signal<EmpresaProfile>({ nombreEmpresa: '', ubicacion: '', telefono: '', descripcion: '', fotoUrl: '' });
  mode = signal<'view' | 'edit'>('view');
  loading = signal(false);
  isUploading = signal(false); // Estado para la subida de imagen
  errorMessage = signal<string | null>(null);
  successMessage = signal<string | null>(null);
  mandatoryMessage: string | null = null;

  // Calculamos el progreso de la empresa (cada campo vale 20% ahora que incluimos la foto)
  progress = computed(() => {
    const p = this.profile();
    let score = 0;
    if (p.nombreEmpresa?.trim()) score += 20;
    if (p.ubicacion?.trim()) score += 20;
    if (p.telefono?.trim()) score += 20;
    if (p.descripcion?.trim()) score += 20;
    if (p.fotoUrl?.trim()) score += 20;
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
      next: (res: any) => {
        const datos = res.data ? res.data : res;
        if (datos && datos.nombreEmpresa) {
          this.profile.set(datos);
          this.mode.set('view');
        } else {
          this.mode.set('edit');
        }
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error cargando perfil:', err);
        this.mode.set('edit');
        this.loading.set(false);
      }
    });
  }

  // Método para gestionar la subida de imagen a Supabase
  async onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (!file) return;

    try {
      this.isUploading.set(true);
      this.errorMessage.set(null);

      const url = await this.supabaseService.uploadImage(file);

      // Actualizamos el signal local
      this.profile.update(p => ({ ...p, fotoUrl: url }));
      this.successMessage.set('Imagen cargada. No olvides guardar los cambios.');
      setTimeout(() => this.successMessage.set(null), 3000);

    } catch (err) {
      this.errorMessage.set('Error al subir la imagen a la nube.');
    } finally {
      this.isUploading.set(false);
    }
  }

  activarEdicion(): void {
    this.mode.set('edit');
  }

  cancelarEdicion(): void {
    this.mode.set('view');
    this.cargarPerfil();
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
      next: (res: any) => {
        const datos = res.data ? res.data : res;
        this.profile.set(datos);
        this.mode.set('view');
        this.tokenStorage.saveProfileCompleted(true);
        this.successMessage.set('Perfil de empresa actualizado con éxito.');

        setTimeout(() => this.successMessage.set(null), 3000);

        if (isNew) {
          this.router.navigate(['/empresa/ofertas/mis-ofertas']);
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
