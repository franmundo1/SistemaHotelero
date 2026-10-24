import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Api } from '../../../core/services/api';
import { Habitacion } from '../models/habitacion';

@Injectable({
  providedIn: 'root',
})
export class HabitacionesService {
  private readonly api = inject(Api);

  listar(): Observable<Habitacion[]> {
    return this.api.get<Habitacion[]>('/api/habitaciones');
  }
}
