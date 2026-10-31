import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Api } from '../../../core/services/api';
import { CanalReserva } from '../models/reserva';

@Injectable({
  providedIn: 'root',
})
export class CanalesReservaService {
  private readonly api = inject(Api);
  private readonly url = '/canalesReserva';

  getAll(): Observable<CanalReserva[]> {
    return this.api.get<CanalReserva[]>(this.url);
  }
}
