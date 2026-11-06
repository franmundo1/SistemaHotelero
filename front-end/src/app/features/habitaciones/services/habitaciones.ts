import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Api } from '../../../core/services/api';
import {
  EstadoLimpieza,
  Habitacion,
  HabitacionLimpieza,
  HabitacionRequest,
} from '../models/habitacion';

@Injectable({
  providedIn: 'root',
})
export class HabitacionesService {
  private readonly api = inject(Api);
  private readonly url = '/api/habitaciones';

  listar(): Observable<Habitacion[]> {
    return this.api.get<Habitacion[]>(this.url);
  }

  getById(idExterno: string): Observable<Habitacion> {
    return this.api.get<Habitacion>(`${this.url}/${idExterno}`);
  }

  getDisponibles(fechaEntrada: string, fechaSalida: string): Observable<Habitacion[]> {
    return this.api.get<Habitacion[]>(`${this.url}/disponibles-por-fecha`, { fechaEntrada, fechaSalida });
  }

  listarParaLimpieza(): Observable<HabitacionLimpieza[]> {
    return this.api.get<HabitacionLimpieza[]>(`${this.url}/limpieza`);
  }

  cambiarEstadoLimpieza(
    numero: string,
    estadoLimpieza: EstadoLimpieza,
  ): Observable<HabitacionLimpieza> {
    return this.api.patch<HabitacionLimpieza>(
      `${this.url}/limpieza/${numero}/estado-limpieza`,
      {},
      { estadoLimpieza },
    );
  }

  create(habitacion: HabitacionRequest): Observable<Habitacion> {
    return this.api.post<Habitacion>(this.url, habitacion);
  }

  update(idExterno: string, cambios: Partial<HabitacionRequest>): Observable<Habitacion> {
    return this.api.patch<Habitacion>(`${this.url}/${idExterno}`, cambios);
  }

  delete(idExterno: string): Observable<void> {
    return this.api.delete(`${this.url}/${idExterno}`);
  }
}
