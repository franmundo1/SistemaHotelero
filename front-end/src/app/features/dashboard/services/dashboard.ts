import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Api } from '../../../core/services/api';
import { DashboardResumen, GraficoDinamico, GraficoRequest } from '../models/dashboard';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  private readonly api = inject(Api);

  obtenerResumen(): Observable<DashboardResumen> {
    return this.api.get<DashboardResumen>('/api/dashboard/resumen');
  }

  // Los filtros viajan como query params: ?tipoGrafico=TORTA&metrica=INGRESOS...
  generarGrafico(filtros: GraficoRequest): Observable<GraficoDinamico> {
    return this.api.get<GraficoDinamico>('/api/dashboard/grafico', { ...filtros });
  }
}
