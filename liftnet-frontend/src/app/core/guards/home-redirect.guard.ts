import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { TokenStorageService } from '../services/token-storage.service';

export const homeRedirectGuard: CanActivateFn = () => {
  const tokenStorage = inject(TokenStorageService);
  const router = inject(Router);

  if (!tokenStorage.isLoggedIn()) {
    return true; // Usuario no autenticado → renderiza HomeComponent
  }

  const role = tokenStorage.currentRole();
  if (role === 'POSTULANTE') {
    router.navigate(['/ofertas']);
    return false;
  }
  if (role === 'EMPRESA') {
    router.navigate(['/empresa/ofertas/mis-ofertas']);
    return false;
  }
  if (role === 'ADMIN') {
    router.navigate(['/admin']);
    return false;
  }

  return true; // Rol desconocido: muestra Home
};
