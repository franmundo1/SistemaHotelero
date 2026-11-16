import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { ApiError } from '../../../../core/models/api-error';
import { GraficoDinamico } from '../../models/dashboard';
import { DashboardService } from '../../services/dashboard';

// Fecha local en yyyy-mm-dd
function fechaLocal(fecha: Date): string {
  return new Date(fecha.getTime() - fecha.getTimezoneOffset() * 60_000).toISOString().slice(0, 10);
}

@Component({
  selector: 'app-ingresos-canal',
  templateUrl: './ingresos-canal.html',
  styleUrl: './ingresos-canal.css',
})
export class IngresosCanal implements OnInit {
  private readonly dashboardService = inject(DashboardService);

  readonly datos = signal<GraficoDinamico | null>(null);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  // Cada canal con su porcentaje respecto del mayor, para el ancho de la barra
  readonly barras = computed(() => {
    const datos = this.datos();
    if (datos === null) {
      return [];
    }
    const maximo = Math.max(...datos.valores, 1);
    // El back manda el canal en mayusculas (WHATSAPP): se muestra como Whatsapp
    return datos.etiquetas.map((etiqueta, i) => ({
      etiqueta: etiqueta.charAt(0) + etiqueta.slice(1).toLowerCase(),
      valor: datos.valores[i],
      ancho: (datos.valores[i] * 100) / maximo,
    }));
  });

  private readonly moneda = new Intl.NumberFormat('es-AR', {
    style: 'currency',
    currency: 'ARS',
    maximumFractionDigits: 0,
  });

  ngOnInit(): void {
    this.cargar();
  }

  // Reutiliza el endpoint del dashboard dinamico con una consulta fija: ingresos del mes por canal
  cargar(): void {
    const hoy = new Date();
    this.cargando.set(true);
    this.error.set(null);

    this.dashboardService
      .generarGrafico({
        tipoGrafico: 'BARRAS',
        metrica: 'INGRESOS',
        agruparPor: 'CANAL',
        fechaDesde: fechaLocal(new Date(hoy.getFullYear(), hoy.getMonth(), 1)),
        fechaHasta: fechaLocal(hoy),
        limite: 5,
      })
      .subscribe({
        next: (grafico) => {
          this.datos.set(grafico);
          this.cargando.set(false);
        },
        error: (error: ApiError) => {
          this.error.set(error.mensaje);
          this.cargando.set(false);
        },
      });
  }

  // Lleva a la sección de gráficos personalizados de la misma página
  irAGraficos(): void {
    document.getElementById('graficos-personalizados')?.scrollIntoView({ behavior: 'smooth' });
  }

  formatear(valor: number): string {
    return this.moneda.format(valor);
  }
}
