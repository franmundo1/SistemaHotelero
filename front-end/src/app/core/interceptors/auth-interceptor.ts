import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { SesionService } from '../auth/sesion.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const sesionService = inject(SesionService);
  const token = sesionService.obtenerToken();

  if (token === null || req.url.includes('/auth/login')) {
    return next(req);
  }

  const peticionAutenticada = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`,
    },
  });

  return next(peticionAutenticada);
};
