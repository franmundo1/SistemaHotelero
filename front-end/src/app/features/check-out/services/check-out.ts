import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Api } from '../../../core/services/api';
import {
  CheckOutRequest,
  CheckOutResponse,
  CheckOutResumen,
} from '../models/check-out';

@Injectable({
  providedIn: 'root',
})
export class CheckOutService {
  private readonly api = inject(Api);
  private readonly url = '/api/checkOut';

  obtenerResumen(idReserva: string): Observable<CheckOutResumen> {
    return this.api.get<CheckOutResumen>(`${this.url}/resumen/${idReserva}`);
  }

  registrar(datos: CheckOutRequest): Observable<CheckOutResponse> {
    return this.api.post<CheckOutResponse>(this.url, datos);
  }
}
