// Error normalizado que reciben todas las pantallas (lo arma errorInterceptor)
export interface ApiError {
  status: number; // 0 = el backend no respondio
  mensaje: string; // texto listo para mostrar al usuario
  validationErrors?: Record<string, string>; // errores por campo que manda el back (400)
}

// Forma del ApiErrorResponse de Spring Boot
export interface ApiErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  validationErrors?: Record<string, string>;
}
