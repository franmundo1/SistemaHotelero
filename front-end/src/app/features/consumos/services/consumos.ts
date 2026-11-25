import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Api } from '../../../core/services/api';
import {
  Consumo,
  ConsumoLoteRequest,
  ConsumoRequest,
} from '../models/consumo';

@Injectable({
  providedIn: 'root',
})
export class ConsumosService {
  private readonly api = inject(Api);
  private readonly url = '/api/consumos';

  registrar(datos: ConsumoRequest): Observable<Consumo> {
    return this.api.post<Consumo>(this.url, datos);
  }

  registrarLote(datos: ConsumoLoteRequest): Observable<Consumo[]> {
    return this.api.post<Consumo[]>(`${this.url}/lote`, datos);
  }

  listarPorReserva(idReserva: string): Observable<Consumo[]> {
    return this.api.get<Consumo[]>(`${this.url}/reserva/${idReserva}`);
  }

}


