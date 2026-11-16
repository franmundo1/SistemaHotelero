import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Api } from '../../../core/services/api';
import { Pago } from '../models/pago';

@Injectable({
  providedIn: 'root',
})
export class PagosService {
  private readonly api = inject(Api);

  listarPorReserva(idReserva: string): Observable<Pago[]> {
    return this.api.get<Pago[]>(`/pagos/reserva/${idReserva}`);
  }
}
