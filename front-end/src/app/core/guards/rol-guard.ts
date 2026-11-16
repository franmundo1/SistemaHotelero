import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { SesionService } from '../auth/sesion.service';

// Cada ruta indica en data.roles quienes pueden entrar
export const rolGuard: CanActivateFn = (route) => {
  const sesionService = inject(SesionService);
  const router = inject(Router);
  const roles: string[] = route.data['roles'] ?? [];

  if (sesionService.tieneAlgunRol(roles)) {
    return true;
  }

  return router.createUrlTree(['/sin-permiso']);
};
