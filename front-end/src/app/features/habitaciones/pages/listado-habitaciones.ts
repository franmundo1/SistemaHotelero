import { CurrencyPipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { ApiError } from '../../../core/models/api-error';
import { Habitacion } from '../models/habitacion';
import { HabitacionesService } from '../services/habitaciones';

@Component({
  selector: 'app-listado-habitaciones',
  imports: [CurrencyPipe],
  templateUrl: './listado-habitaciones.html',
  styleUrl: './listado-habitaciones.css',
})
export class ListadoHabitaciones implements OnInit {
  private readonly habitacionesService = inject(HabitacionesService);

  readonly habitaciones = signal<Habitacion[]>([]);
  readonly cargando = signal(false);
  readonly error = signal<string | null>(null);

  ngOnInit(): void {
    this.cargarHabitaciones();
  }

  cargarHabitaciones(): void {
    this.cargando.set(true);
    this.error.set(null);

    this.habitacionesService.listar().subscribe({
      next: (habitaciones) => {
        this.habitaciones.set(habitaciones);
        this.cargando.set(false);
      },
      error: (error: ApiError) => {
        this.error.set(error.mensaje);
        this.cargando.set(false);
      },
    });
  }

  mostrarEstado(estado: string): string {
    const texto = estado.replaceAll('_', ' ').toLowerCase();

    return texto.charAt(0).toUpperCase() + texto.slice(1);
  }
}
