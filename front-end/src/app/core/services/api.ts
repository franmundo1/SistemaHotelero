import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

type Params = HttpParams | Record<string, string | number | boolean | readonly (string | number | boolean)[]>;

// Servicio base: unico lugar que conoce la URL del backend.
// Los servicios de cada feature lo usan con rutas relativas, ej: api.get<Habitacion[]>('/api/habitaciones')
@Service()
export class Api {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiUrl;

  get<T>(ruta: string, params?: Params): Observable<T> {
    return this.http.get<T>(`${this.baseUrl}${ruta}`, { params });
  }

  post<T>(ruta: string, body: unknown): Observable<T> {
    return this.http.post<T>(`${this.baseUrl}${ruta}`, body);
  }

  put<T>(ruta: string, body: unknown): Observable<T> {
    return this.http.put<T>(`${this.baseUrl}${ruta}`, body);
  }

  patch<T>(ruta: string, body: unknown = {}, params?: Params): Observable<T> {
    return this.http.patch<T>(`${this.baseUrl}${ruta}`, body, { params });
  }

  delete<T = void>(ruta: string): Observable<T> {
    return this.http.delete<T>(`${this.baseUrl}${ruta}`);
  }
}
