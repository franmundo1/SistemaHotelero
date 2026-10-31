import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Api } from '../../../core/services/api';
import { Habitacion, HabitacionRequest } from '../models/habitacion';

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

  // Activas, sin mantenimiento y sin reservas en el rango (la capacidad se filtra en la pantalla)
  getDisponibles(fechaEntrada: string, fechaSalida: string): Observable<Habitacion[]> {
    return this.api.get<Habitacion[]>(`${this.url}/disponibles-por-fecha`, { fechaEntrada, fechaSalida });
  }

  create(habitacion: HabitacionRequest): Observable<Habitacion> {
    return this.api.post<Habitacion>(this.url, habitacion);
  }

  // PATCH: solo se mandan los campos que cambiaron
  update(idExterno: string, cambios: Partial<HabitacionRequest>): Observable<Habitacion> {
    return this.api.patch<Habitacion>(`${this.url}/${idExterno}`, cambios);
  }

  // Baja logica: el back pasa la habitacion a inactiva
  delete(idExterno: string): Observable<void> {
    return this.api.delete(`${this.url}/${idExterno}`);
  }
}
