import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { ApiError } from '../../../../core/models/api-error';
import { Reserva } from '../../../reservas/models/reserva';
import { ReservasService } from '../../../reservas/services/reservas';

const UN_DIA = 86_400_000;

// Fecha local en yyyy-mm-dd (toISOString usa UTC y de noche daria el dia siguiente)
function fechaLocalHoy(): string {
  const hoy = new Date();
  return new Date(hoy.getTime() - hoy.getTimezoneOffset() * 60_000).toISOString().slice(0, 10);
}

@Component({
  selector: 'app-llegadas-hoy',
  imports: [RouterLink],
  templateUrl: './llegadas-hoy.html',
  styleUrl: './llegadas-hoy.css',
})
export class LlegadasHoy implements OnInit {
  private readonly reservasService = inject(ReservasService);

  readonly llegadas = signal<Reserva[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  ngOnInit(): void {
    this.cargar();
  }

  // Llegadas de hoy: confirmadas que todavia no entraron y en curso que ya hicieron check-in hoy
  cargar(): void {
    const hoy = fechaLocalHoy();
    this.cargando.set(true);
    this.error.set(null);

    forkJoin([this.reservasService.listarConfirmadas(), this.reservasService.listarEnCurso()]).subscribe({
      next: ([confirmadas, enCurso]) => {
        this.llegadas.set([...confirmadas, ...enCurso].filter((reserva) => reserva.fechaEntrada === hoy));
        this.cargando.set(false);
      },
      error: (error: ApiError) => {
        this.error.set(error.mensaje);
        this.cargando.set(false);
      },
    });
  }

  noches(reserva: Reserva): number {
    const entrada = new Date(reserva.fechaEntrada).getTime();
    const salida = new Date(reserva.fechaSalida).getTime();
    return Math.max(Math.round((salida - entrada) / UN_DIA), 1);
  }

  textoEstado(estado: string): string {
    return estado === 'EN_CURSO' ? 'En curso' : 'Confirmada';
  }
}
