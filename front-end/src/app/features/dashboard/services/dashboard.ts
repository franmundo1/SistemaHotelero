import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Api } from '../../../core/services/api';
import { DashboardResumen } from '../models/dashboard';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  private readonly api = inject(Api);

  obtenerResumen(): Observable<DashboardResumen> {
    return this.api.get<DashboardResumen>('/api/dashboard/resumen');
  }
}
