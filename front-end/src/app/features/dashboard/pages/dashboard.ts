import {
  afterRenderEffect,
  Component,
  computed,
  effect,
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
import { toSignal } from '@angular/core/rxjs-interop';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { SesionService } from '../../../core/auth/sesion.service';
import { ApiError } from '../../../core/models/api-error';
import { GraficoDinamicoCard } from '../components/grafico-dinamico/grafico-dinamico';
import {
  AGRUPACIONES,
  AGRUPACIONES_POR_METRICA,
  DashboardResumen,
  GraficoDinamico,
  METRICAS,
  TIPOS_GRAFICO,
  TipoAgrupacion,
  TipoGrafico,
  TipoMetrica,
} from '../models/dashboard';
import { DashboardService } from '../services/dashboard';

// Fecha de hoy y del primer dia del mes en formato yyyy-mm-dd (el que usa el input date)
const hoy = new Date();
const HOY = hoy.toISOString().slice(0, 10);
const PRIMERO_DEL_MES = new Date(hoy.getFullYear(), hoy.getMonth(), 1).toISOString().slice(0, 10);

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
  imports: [ReactiveFormsModule, GraficoDinamicoCard],
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

  // ---- Graficos dinamicos (H26) ----
  protected readonly tiposGrafico = TIPOS_GRAFICO;
  protected readonly metricas = METRICAS;

  readonly filtros = new FormGroup({
    tipoGrafico: new FormControl<TipoGrafico>('TORTA', { nonNullable: true, validators: [Validators.required] }),
    metrica: new FormControl<TipoMetrica>('INGRESOS', { nonNullable: true, validators: [Validators.required] }),
    agruparPor: new FormControl<TipoAgrupacion>('HABITACION', { nonNullable: true, validators: [Validators.required] }),
    fechaDesde: new FormControl(PRIMERO_DEL_MES, { nonNullable: true, validators: [Validators.required] }),
    fechaHasta: new FormControl(HOY, { nonNullable: true, validators: [Validators.required] }),
    limite: new FormControl(5, { nonNullable: true, validators: [Validators.required, Validators.min(1), Validators.max(10)] }),
  });

  private readonly metricaElegida = toSignal(this.filtros.controls.metrica.valueChanges, { initialValue: 'INGRESOS' as TipoMetrica });
  private readonly fechaDesde = toSignal(this.filtros.controls.fechaDesde.valueChanges, { initialValue: PRIMERO_DEL_MES });
  private readonly fechaHasta = toSignal(this.filtros.controls.fechaHasta.valueChanges, { initialValue: HOY });

  // "Agrupar por" solo muestra las opciones validas para la metrica elegida
  readonly agrupaciones = computed(() =>
    AGRUPACIONES.filter((opcion) => AGRUPACIONES_POR_METRICA[this.metricaElegida()].includes(opcion.valor))
  );

  // Las fechas vienen en yyyy-mm-dd, asi que se pueden comparar como texto
  readonly fechasInvalidas = computed(() => this.fechaDesde() > this.fechaHasta());

  readonly graficosDinamicos = signal<{ id: number; grafico: GraficoDinamico }[]>([]);
  readonly generando = signal(false);
  readonly mensajeGrafico = signal<string | null>(null);
  private proximoId = 1;

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

    // Si la agrupacion elegida no sirve para la nueva metrica, se pasa a la primera valida
    effect(() => {
      const opciones = this.agrupaciones();
      const actual = this.filtros.controls.agruparPor.value;

      if (!opciones.some((opcion) => opcion.valor === actual)) {
        this.filtros.controls.agruparPor.setValue(opciones[0].valor);
      }
    });
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

  onGenerarGrafico(): void {
    if (this.filtros.invalid) {
      this.filtros.markAllAsTouched();
      return;
    }
    // Con las fechas invertidas no se llama a la API
    if (this.fechasInvalidas()) {
      return;
    }

    this.generando.set(true);
    this.mensajeGrafico.set(null);

    this.dashboardService.generarGrafico(this.filtros.getRawValue()).subscribe({
      next: (grafico) => {
        if (grafico.etiquetas.length === 0) {
          this.mensajeGrafico.set('No hay datos para el período seleccionado.');
        } else {
          // El nuevo va primero para que se vea sin tener que bajar
          this.graficosDinamicos.update((lista) => [{ id: this.proximoId++, grafico }, ...lista]);
        }
        this.generando.set(false);
      },
      error: (error: ApiError) => {
        this.mensajeGrafico.set(error.mensaje);
        this.generando.set(false);
      },
    });
  }

  onQuitarGrafico(id: number): void {
    this.graficosDinamicos.update((lista) => lista.filter((item) => item.id !== id));
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
