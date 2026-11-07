import {
  afterRenderEffect,
  Component,
  computed,
  ElementRef,
  inject,
  OnDestroy,
  OnInit,
  signal,
  viewChild,
} from '@angular/core';
import {
  ArcElement,
  BarController,
  BarElement,
  CategoryScale,
  Chart,
  DoughnutController,
  Legend,
  LinearScale,
  Tooltip,
} from 'chart.js';
import { SesionService } from '../../../core/auth/sesion.service';
import { ApiError } from '../../../core/models/api-error';
import { DashboardResumen } from '../models/dashboard';
import { DashboardService } from '../services/dashboard';

// Solo se registran las piezas de Chart.js que usan estos graficos
Chart.register(
  ArcElement,
  BarController,
  BarElement,
  CategoryScale,
  DoughnutController,
  Legend,
  LinearScale,
  Tooltip,
);

// Mismos colores que los estados del listado de habitaciones
const COLORES = {
  verde: '#16a34a',
  rojo: '#dc2626',
  ambar: '#d97706',
  azul: '#2563eb',
  gris: '#94a3b8',
};

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit, OnDestroy {
  private readonly dashboardService = inject(DashboardService);
  private readonly sesionService = inject(SesionService);

  // Los canvas aparecen recien cuando llega el resumen (estan dentro de un @if)
  private readonly lienzoOcupacion = viewChild<ElementRef<HTMLCanvasElement>>('ocupacion');
  private readonly lienzoReservas = viewChild<ElementRef<HTMLCanvasElement>>('reservas');
  private readonly lienzoLimpieza = viewChild<ElementRef<HTMLCanvasElement>>('limpieza');

  private graficos: Chart[] = [];

  readonly resumen = signal<DashboardResumen | null>(null);
  readonly cargando = signal(false);
  readonly error = signal<string | null>(null);

  readonly nombreUsuario = this.sesionService.nombreUsuario;

  readonly porcentajeOcupacion = computed(() => {
    const resumen = this.resumen();

    if (resumen === null || resumen.habitacionesTotales === 0) {
      return 0;
    }

    return Math.round((resumen.habitacionesOcupadas * 100) / resumen.habitacionesTotales);
  });

  readonly pendientesLimpieza = computed(() => {
    const resumen = this.resumen();

    return resumen === null ? 0 : resumen.habitacionesSucias + resumen.habitacionesEnLimpieza;
  });

  // El back no manda las limpias: son las activas que no estan sucias ni en limpieza
  readonly habitacionesLimpias = computed(() => {
    const resumen = this.resumen();

    return resumen === null
      ? 0
      : Math.max(resumen.habitacionesTotales - this.pendientesLimpieza(), 0);
  });

  constructor() {
    // Se redibuja cada vez que cambia el resumen o aparecen los canvas
    afterRenderEffect(() => this.dibujarGraficos());
  }

  ngOnInit(): void {
    this.cargarResumen();
  }

  ngOnDestroy(): void {
    this.destruirGraficos();
  }

  cargarResumen(): void {
    this.cargando.set(true);
    this.error.set(null);

    this.dashboardService.obtenerResumen().subscribe({
      next: (resumen) => {
        this.resumen.set(resumen);
        this.cargando.set(false);
      },
      error: (error: ApiError) => {
        this.error.set(error.mensaje);
        this.cargando.set(false);
      },
    });
  }

  private dibujarGraficos(): void {
    const resumen = this.resumen();
    const ocupacion = this.lienzoOcupacion();
    const reservas = this.lienzoReservas();
    const limpieza = this.lienzoLimpieza();

    this.destruirGraficos();

    if (resumen === null || !ocupacion || !reservas || !limpieza) {
      return;
    }

    this.graficos = [
      this.crearDona(
        ocupacion.nativeElement,
        ['Disponibles', 'Ocupadas', 'Mantenimiento'],
        [
          resumen.habitacionesDisponibles,
          resumen.habitacionesOcupadas,
          resumen.habitacionesMantenimiento,
        ],
        [COLORES.verde, COLORES.rojo, COLORES.ambar],
      ),
      new Chart(reservas.nativeElement, {
        type: 'bar',
        data: {
          labels: ['Confirmadas', 'En curso', 'Finalizadas', 'Canceladas'],
          datasets: [
            {
              label: 'Reservas',
              data: [
                resumen.reservasConfirmadas,
                resumen.reservasEnCurso,
                resumen.reservasFinalizadas,
                resumen.reservasCanceladas,
              ],
              backgroundColor: [COLORES.azul, COLORES.verde, COLORES.gris, COLORES.rojo],
              borderRadius: 6,
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { display: false } },
          scales: { y: { beginAtZero: true, ticks: { precision: 0 } } },
        },
      }),
      this.crearDona(
        limpieza.nativeElement,
        ['Limpias', 'Sucias', 'En limpieza'],
        [this.habitacionesLimpias(), resumen.habitacionesSucias, resumen.habitacionesEnLimpieza],
        [COLORES.verde, COLORES.rojo, COLORES.ambar],
      ),
    ];
  }

  private crearDona(
    lienzo: HTMLCanvasElement,
    etiquetas: string[],
    valores: number[],
    colores: string[],
  ): Chart {
    return new Chart(lienzo, {
      type: 'doughnut',
      data: {
        labels: etiquetas,
        datasets: [{ data: valores, backgroundColor: colores, borderWidth: 0 }],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        cutout: '65%',
        plugins: { legend: { position: 'bottom' } },
      },
    });
  }

  private destruirGraficos(): void {
    this.graficos.forEach((grafico) => grafico.destroy());
    this.graficos = [];
  }
}
