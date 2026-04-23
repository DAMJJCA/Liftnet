import { CanActivateFn, ActivatedRouteSnapshot, Router } from '@angular/router';
import { inject } from '@angular/core';

import { TokenStorageService } from '../services/token-storage.service';

export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const tokenStorage = inject(TokenStorageService);
  const router = inject(Router);

  const expectedRoles = route.data['roles'] as string[] | undefined;
  const userRole = tokenStorage.getRole();

  // Si no hay roles definidos, permitir
  if (!expectedRoles || expectedRoles.length === 0) {
    return true;
  }

  if (userRole && expectedRoles.includes(userRole)) {
    return true;
  }

  // Rol no permitido
  router.navigate(['/login']);
  return false;
};
