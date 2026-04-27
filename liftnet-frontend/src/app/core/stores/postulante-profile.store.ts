import { Injectable, signal, inject, computed } from '@angular/core';
import { Router } from '@angular/router';

import { PostulanteService, PostulanteProfile } from '../services/postulante.service';
import { TokenStorageService } from '../services/token-storage.service';

@Injectable({ providedIn: 'root' })
export class PostulanteProfileStore {

  private postulanteSvc = inject(PostulanteService);
  private tokenStorage = inject(TokenStorageService);
  private router = inject(Router);

  // ==========================
  // STATE BASE
  // ==========================
  profile = signal<PostulanteProfile>({
    nombre: '',
    apellidos: '',
    email: '',
    ubicacion: '',
    telefono: '',
    bio: '',
    disponible: false,
    experiencias: [],
    certificaciones: []
  });

  mode = signal<'view' | 'edit'>('edit');
  loading = signal(false);
  errorMessage = signal<string | null>(null);
  successMessage = signal<string | null>(null);

  // ==========================
  // SECCIONES DEL PERFIL
  // ==========================
  sectionsStatus = computed(() => {
    const p = this.profile();

    return {
      basicInfo: !!(p.nombre && p.apellidos && p.telefono),
      experiences: Array.isArray(p.experiencias) && p.experiencias.length > 0,
      certifications: Array.isArray(p.certificaciones) && p.certificaciones.length > 0
    };
  });

  // ==========================
  // CAPACIDADES (clave InfoJobs)
  // ==========================
  capabilities = computed(() => {
    const sections = this.sectionsStatus();

    return {
      canViewOffers: sections.basicInfo,
      canApply: sections.certifications,
      profileReady: sections.basicInfo && sections.certifications
    };
  });

  // ==========================
  // CARGAR PERFIL
  // ==========================
  cargarPerfil(): void {
    this.loading.set(true);

    this.postulanteSvc.getProfile().subscribe({
      next: res => {
        if (res.data) {
          this.profile.set(res.data);
          this.mode.set('view');
        }
        this.loading.set(false);
      },
      error: () => {
        // Perfil aún no existe → edición
        this.mode.set('edit');
        this.loading.set(false);
      }
    });
  }

  // ==========================
  // GUARDAR PERFIL
  // ==========================
  guardarPerfil(): void {
    this.loading.set(true);
    this.errorMessage.set(null);
    this.successMessage.set(null);

    const isNew = !this.tokenStorage.isProfileCompleted();

    const request$ = isNew
      ? this.postulanteSvc.createProfile(this.profile())
      : this.postulanteSvc.updateProfile(this.profile());

    request$.subscribe({
      next: res => {
        this.profile.set(res.data);
        this.loading.set(false);
        this.mode.set('view');

        // ✅ Marcar que YA EXISTE perfil
        this.tokenStorage.saveProfileCompleted(true);

        if (isNew) {
          this.successMessage.set('Perfil creado correctamente');
          // ✅ NO redirigir a lo bruto
          // El usuario decide qué hacer ahora
        } else {
          this.successMessage.set('Perfil actualizado correctamente');
        }
      },
      error: () => {
        this.errorMessage.set('No se pudo guardar el perfil');
        this.loading.set(false);
      }
    });
  }

  // ==========================
  // DISPONIBILIDAD
  // ==========================
  cambiarDisponibilidad(): void {
    const actual = this.profile().disponible;

    this.postulanteSvc.updateDisponibilidad(!actual).subscribe({
      next: () => {
        this.profile.update(p => ({ ...p, disponible: !actual }));
      },
      error: () => {
        this.errorMessage.set('No se pudo cambiar la disponibilidad');
      }
    });
  }
}
