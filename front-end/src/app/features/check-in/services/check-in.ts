import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Api } from '../../../core/services/api';
import { CheckIn, CheckInRequest } from '../models/check-in';

@Injectable({
  providedIn: 'root',
})
export class CheckInService {
  private readonly api = inject(Api);
  private readonly url = '/api/checkIn';

  registrar(datos: CheckInRequest): Observable<CheckIn> {
    return this.api.post<CheckIn>(this.url, datos);
  }
}
