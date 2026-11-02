import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Api } from '../../../core/services/api';
import {
  CancelarReservaRequest,
  Reserva,
  ReservaRequest,
} from '../models/reserva';

@Injectable({
  providedIn: 'root',
})
export class ReservasService {
  private readonly api = inject(Api);
  private readonly url = '/api/reservas';

  create(reserva: ReservaRequest): Observable<Reserva> {
    return this.api.post<Reserva>(this.url, reserva);
  }

  listar(): Observable<Reserva[]> {
    return this.api.get<Reserva[]>(this.url);
  }

  listarConfirmadas(): Observable<Reserva[]> {
    return this.api.get<Reserva[]>(`${this.url}/confirmadas`);
  }

  cancelar(
    idExterno: string,
    motivoCancelacion: string
  ): Observable<Reserva> {
    const datos: CancelarReservaRequest = {
      motivoCancelacion,
    };

    return this.api.patch<Reserva>(
      `${this.url}/${idExterno}/cancelar`,
      datos
    );
  }
}
