import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { TokenStorageService } from '../services/token-storage.service';

export const profileGuard: CanActivateFn = () => {
  const tokenStorage = inject(TokenStorageService);
  const router = inject(Router);

  // si no está logueado, no hacemos nada
  if (!tokenStorage.isLoggedIn()) {
    return true;
  }

  // perfil incompleto → bloquear
  if (!tokenStorage.isProfileCompleted()) {
    const role = tokenStorage.getRole();

    if (role === 'EMPRESA') {
      router.navigate(['/empresa/perfil']);
      return false;
    }

    if (role === 'POSTULANTE') {
      router.navigate(['/postulante/perfil']);
      return false;
    }
  }
  return true;
};
