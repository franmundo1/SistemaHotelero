import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Api } from '../../../core/services/api';
import { Huesped, HuespedRequest } from '../models/huesped';

@Injectable({
  providedIn: 'root',
})
export class HuespedesService {
  private readonly api = inject(Api);
  private readonly url = '/api/huespedes';

  getAll(): Observable<Huesped[]> {
    return this.api.get<Huesped[]>(this.url);
  }

  create(huesped: HuespedRequest): Observable<Huesped> {
    return this.api.post<Huesped>(this.url, huesped);
  }

  update(idExterno: string, huesped: HuespedRequest): Observable<Huesped> {
    return this.api.put<Huesped>(`${this.url}/${idExterno}`, huesped);
  }

  // El back borra el registro de la base (si tiene reservas responde 409)
  delete(idExterno: string): Observable<void> {
    return this.api.delete(`${this.url}/${idExterno}`);
  }
}
