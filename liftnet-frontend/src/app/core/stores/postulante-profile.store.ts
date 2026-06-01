import { Injectable, signal, inject, computed } from '@angular/core';
import { Router } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { PostulanteService, PostulanteProfile } from '../services/postulante.service';
import { ExperienciasService } from '../services/experiencias.service';
import { CertificacionesService } from '../services/certificaciones.service';
import { TokenStorageService } from '../services/token-storage.service';
import { Experiencia } from '../services/postulante.service';
import { Certificacion } from '../services/postulante.service';

@Injectable({ providedIn: 'root' })
export class PostulanteProfileStore {

  private postulanteSvc = inject(PostulanteService);
  private experienciasSvc = inject(ExperienciasService);
  private certificacionesSvc = inject(CertificacionesService);
  private tokenStorage = inject(TokenStorageService);
  private router = inject(Router);

  // STATE BASE
  profile = signal<PostulanteProfile>({
    nombre: '', apellidos: '', email: '', ubicacion: '', telefono: '', bio: '', disponible: false,
    experiencias: [], certificaciones: []
  });

  // Indica si el perfil ya existe en BD (independiente de si está completo o no)
  profileExistsInDB = signal<boolean>(false);

  mode = signal<'view' | 'edit'>('view');
  loading = signal(false);
  errorMessage = signal<string | null>(null);
  successMessage = signal<string | null>(null);

  // CALCULAMOS EL PROGRESO REAL
  sectionsStatus = computed(() => {
    const p = this.profile() || {} as any;
    return {
      basicInfo: !!(p.nombre && p.apellidos && p.telefono),
      experiences: Array.isArray(p.experiencias) && p.experiencias.length > 0,
      certifications: Array.isArray(p.certificaciones) && p.certificaciones.length > 0
    };
  });

  progress = computed(() => {
    const sections = this.sectionsStatus();
    const values = Object.values(sections);
    const completed = values.filter(v => v).length;
    return Math.round((completed / values.length) * 100);
  });

// CARGAR TODO (Perfil + Exp + Cert) EN PARALELO Y A PRUEBA DE FALLOS
  cargarTodo(): void {
    this.loading.set(true);

    forkJoin({
      // Si alguno falla, capturamos el error y devolvemos null para que el resto siga cargando
      perfil: this.postulanteSvc.getProfile().pipe(
        tap(() => this.profileExistsInDB.set(true)),
        catchError((e) => { this.profileExistsInDB.set(false); console.error('Error Perfil', e); return of(null); })
      ),
      exp: this.experienciasSvc.getMisExperiencias(0, 50).pipe(catchError((e) => { console.error('Error Exp', e); return of(null); })),
      cert: this.certificacionesSvc.getMisCertificaciones(0, 50).pipe(catchError((e) => { console.error('Error Cert', e); return of(null); }))
    }).subscribe({
      next: (res: any) => {
        console.log(' Datos crudos recibidos del backend:', res);

        // 1. Extraer datos básicos de forma segura (si es null, usamos objeto vacío {})
        const baseData = res.perfil?.data || res.perfil || {};

        // 2. Extraer arrays buscando en todas las posibles rutas del JSON de Spring Boot
        const experiencias = (res.exp?.data?.content || res.exp?.content || res.exp?.data || []) as any[];
        const certificaciones = (res.cert?.data?.content || res.cert?.content || res.cert?.data || []) as any[];

        // 3. Fusionamos los datos nuevos con lo que ya tuviéramos en el estado
        this.profile.set({
          ...this.profile(),
          ...baseData,
          experiencias: experiencias,
          certificaciones: certificaciones
        });

        this.mode.set('view');
        this.loading.set(false);
      }
    });
  }

  // GUARDAR SOLO DATOS BÁSICOS
  guardarDatosBasicos(): void {
    this.loading.set(true);
    this.errorMessage.set(null);
    this.successMessage.set(null);

    // Usamos profileExistsInDB para saber si el perfil ya existe en BD,
    // independientemente del estado de localStorage (evita llamar a createProfile cuando ya existe).
    const isNew = !this.profileExistsInDB();
    const request$ = isNew ? this.postulanteSvc.createProfile(this.profile()) : this.postulanteSvc.updateProfile(this.profile());

    request$.subscribe({
      next: res => {
        this.profile.update(p => ({ ...p, ...(res.data || {}) }));
        this.mode.set('view');
        this.tokenStorage.saveProfileCompleted(true);
        this.profileExistsInDB.set(true);
        this.successMessage.set('Datos básicos guardados correctamente');
        setTimeout(() => this.successMessage.set(null), 3000);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('No se pudo guardar el perfil');
        this.loading.set(false);
      }
    });
  }
}
