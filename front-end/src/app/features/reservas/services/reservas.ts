import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Api } from '../../../core/services/api';
import { Reserva, ReservaRequest } from '../models/reserva';

@Injectable({
  providedIn: 'root',
})
export class ReservasService {
  private readonly api = inject(Api);
  private readonly url = '/api/reservas';

  create(reserva: ReservaRequest): Observable<Reserva> {
    return this.api.post<Reserva>(this.url, reserva);
  }

  listarConfirmadas(): Observable<Reserva[]> {
    return this.api.get<Reserva[]>(`${this.url}/confirmadas`);
  }
}
