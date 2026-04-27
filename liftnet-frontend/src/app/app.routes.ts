import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';
import { profileGuard } from './core/guards/profile.guard';

export const routes: Routes = [

  // =========================
  // PÚBLICAS
  // =========================
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  {
    path: 'login',
    loadComponent: () =>
      import('./auth/login/login.component')
        .then(m => m.LoginComponent),
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./auth/register/register.component')
        .then(m => m.RegisterComponent),
  },

  // =========================
  // POSTULANTE
  // =========================
  {
    path: 'postulante',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['POSTULANTE'] },
    children: [

      // PERFIL (sin profileGuard)
      {
        path: 'perfil',
        loadComponent: () =>
          import('./postulante/profile/postulante-profile.component')
            .then(m => m.PostulanteProfileComponent),
      },

      // POSTULACIONES
      {
        path: 'postulaciones',
        canActivate: [profileGuard],
        loadComponent: () =>
          import('./postulante/postulaciones/postulante-postulaciones.component')
            .then(m => m.PostulacionesComponent),
      },

      // EXPERIENCIAS
      {
        path: 'experiencias',
        canActivate: [profileGuard],
        loadComponent: () =>
          import('./postulante/experiencias/mis-experiencias/mis-experiencias.component')
            .then(m => m.MisExperienciasComponent),
      },

      // CERTIFICACIONES
      {
        path: 'certificaciones',
        canActivate: [profileGuard],
        loadComponent: () =>
          import('./postulante/certificaciones/mis-certificaciones/mis-certificaciones.component')
            .then(m => m.MisCertificacionesComponent),
      }
    ],
  },

  // =========================
  // EMPRESA
  // =========================
  {
    path: 'empresa',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['EMPRESA'] },
    children: [

      // PERFIL (sin profileGuard)
      {
        path: 'perfil',
        loadComponent: () =>
          import('./empresa/profile/empresa-profile.component')
            .then(m => m.EmpresaProfileComponent),
      },

      // OFERTAS
      {
        path: 'ofertas',
        canActivate: [profileGuard],
        children: [
          {
            path: 'lista',
            loadComponent: () =>
              import('./empresa/ofertas/lista-ofertas/lista-ofertas.component')
                .then(m => m.ListaOfertasComponent),
          },
          {
            path: 'mis-ofertas',
            loadComponent: () =>
              import('./empresa/ofertas/mis-ofertas/mis-ofertas.component')
                .then(m => m.MisOfertasComponent),
          },
          {
            path: ':ofertaId/postulaciones',
            loadComponent: () =>
              import('./empresa/postulaciones/empresa-postulaciones.component')
                .then(m => m.EmpresaPostulacionesComponent),
          },
        ],
      },
    ],
  },

  // =========================
  // ADMIN
  // =========================
  {
    path: 'admin',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
    loadComponent: () =>
      import('./admin/admin.component')
        .then(m => m.AdminComponent),
  },

  // =========================
  // FALLBACK
  // =========================
  { path: '**', redirectTo: 'login' },
];
