import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { ApiError, ApiErrorResponse } from '../models/api-error';

// Convierte cualquier error HTTP en un ApiError con un mensaje entendible
export const errorInterceptor: HttpInterceptorFn = (req, next) =>
  next(req).pipe(
    catchError((err: HttpErrorResponse) => throwError(() => aApiError(err)))
  );

function aApiError(err: HttpErrorResponse): ApiError {
  const cuerpo = err.error as Partial<ApiErrorResponse> | null;

  return {
    status: err.status,
    mensaje: mensajePorStatus(err.status, cuerpo?.message),
    validationErrors: cuerpo?.validationErrors ?? undefined,
  };
}

function mensajePorStatus(status: number, mensajeBack?: string): string {
  switch (status) {
    case 0:
      return 'No se pudo conectar con el servidor. Verificá que el backend esté encendido e intentá de nuevo.';
    case 401:
      return 'Tu sesión venció o las credenciales son incorrectas. Iniciá sesión nuevamente.';
    case 403:
      return 'No tenés permisos para realizar esta acción.';
    case 404:
      return mensajeBack || 'No se encontró el recurso solicitado.';
    default:
      if (status >= 500) {
        return 'Ocurrió un error en el servidor. Intentá nuevamente más tarde.';
      }
      // 400 y otros: el back ya manda un mensaje propio
      return mensajeBack || 'No se pudo completar la operación.';
  }
}
