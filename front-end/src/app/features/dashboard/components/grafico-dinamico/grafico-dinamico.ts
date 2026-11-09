import {
  afterRenderEffect,
  Component,
  ElementRef,
  input,
  OnDestroy,
  output,
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
  PieController,
  Tooltip,
} from 'chart.js';
import { GraficoDinamico } from '../../models/dashboard';

Chart.register(
  ArcElement,
  BarController,
  BarElement,
  CategoryScale,
  DoughnutController,
  Legend,
  LinearScale,
  PieController,
  Tooltip,
);

const COLORES = ['#2563eb', '#16a34a', '#d97706', '#dc2626', '#7c3aed', '#94a3b8'];

@Component({
  selector: 'app-grafico-dinamico',
  templateUrl: './grafico-dinamico.html',
  styleUrl: './grafico-dinamico.css',
})
export class GraficoDinamicoCard implements OnDestroy {
  readonly grafico = input.required<GraficoDinamico>();
  readonly quitar = output<void>(); // el padre decide como sacarlo de la lista

  private readonly lienzo = viewChild<ElementRef<HTMLCanvasElement>>('lienzo');
  private chart: Chart | null = null;

  constructor() {
    afterRenderEffect(() => this.dibujar());
  }

  ngOnDestroy(): void {
    this.chart?.destroy();
  }

  onQuitar(): void {
    this.quitar.emit();
  }

  private dibujar(): void {
    const grafico = this.grafico();
    const lienzo = this.lienzo();

    this.chart?.destroy();

    if (!lienzo) {
      return;
    }

    // Torta y dona usan un color por porcion; las barras no necesitan leyenda
    const circular = grafico.tipoGrafico !== 'BARRAS';

    this.chart = new Chart(lienzo.nativeElement, {
      type: grafico.tipoGrafico === 'TORTA' ? 'pie' : grafico.tipoGrafico === 'DONA' ? 'doughnut' : 'bar',
      data: {
        labels: grafico.etiquetas,
        datasets: [
          {
            label: grafico.titulo,
            data: grafico.valores,
            backgroundColor: circular ? COLORES : COLORES[0],
            borderWidth: 0,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: circular, position: 'bottom' } },
      },
    });
  }
}
