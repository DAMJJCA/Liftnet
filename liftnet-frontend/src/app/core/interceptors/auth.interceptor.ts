import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { TokenStorageService } from '../services/token-storage.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenStorage = inject(TokenStorageService);
  const router = inject(Router);

  const token = tokenStorage.getToken();
  const email = tokenStorage.getEmail();

  let authReq = req;

  // Detectamos si es una ruta de autenticación
  const isAuthRoute = req.url.includes('/auth');

  // 1. Inyectar Token solo si existe Y NO es ruta de auth
  if (token && !isAuthRoute) {
    authReq = authReq.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
  }

  // 2. TRUCO MODO DEV: Inyectar ?email= a todas las peticiones (menos auth)
  if (email && req.url.includes('/api/v1') && !isAuthRoute) {
    authReq = authReq.clone({
      params: authReq.params.set('email', email)
    });
  }

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 || error.status === 403) {
        tokenStorage.clear();
        router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
};
