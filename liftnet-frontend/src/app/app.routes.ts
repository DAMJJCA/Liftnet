import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  // RUTAS PÚBLICAS
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
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

  // RUTAS PRIVADAS (POSTULANTE, EMPRESA, ETC.)
  {
    path: 'certificaciones',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./certificaciones/mis-certificaciones/mis-certificaciones.component')
        .then(m => m.MisCertificacionesComponent),
  },
  {
    path: 'experiencias',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./experiencias/mis-experiencias/mis-experiencias.component')
        .then(m => m.MisExperienciasComponent),
  },

  // FALLBACK
  {
    path: '**',
    redirectTo: 'login',
  }
];
