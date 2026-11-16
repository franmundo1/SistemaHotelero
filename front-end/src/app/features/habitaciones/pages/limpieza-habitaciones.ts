import { Component, inject, OnInit, signal } from '@angular/core';
import { ApiError } from '../../../core/models/api-error';
import {
  EstadoLimpieza,
  HabitacionLimpieza,
} from '../models/habitacion';
import { HabitacionesService } from '../services/habitaciones';

@Component({
  selector: 'app-limpieza-habitaciones',
  templateUrl: './limpieza-habitaciones.html',
  styleUrl: './limpieza-habitaciones.css',
})
export class LimpiezaHabitaciones implements OnInit {
  private readonly habitacionesService = inject(HabitacionesService);

  readonly habitaciones = signal<HabitacionLimpieza[]>([]);
  readonly cargando = signal(false);
  readonly actualizando = signal<string | null>(null);
  readonly error = signal<string | null>(null);
  readonly mensaje = signal<string | null>(null);

  ngOnInit(): void {
    this.cargarHabitaciones();
  }

  cargarHabitaciones(): void {
    this.cargando.set(true);
    this.error.set(null);

    this.habitacionesService.listarParaLimpieza().subscribe({
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

  cambiarEstado(habitacion: HabitacionLimpieza): void {
    const nuevoEstado: EstadoLimpieza =
      habitacion.estadoLimpieza === 'SUCIA' ? 'EN_LIMPIEZA' : 'LIMPIA';

    this.actualizando.set(habitacion.numero);
    this.error.set(null);
    this.mensaje.set(null);

    this.habitacionesService
      .cambiarEstadoLimpieza(habitacion.numero, nuevoEstado)
      .subscribe({
        next: (actualizada) => {
          // Si ya quedo limpia no tiene que seguir en la lista
          this.habitaciones.update((habitaciones) =>
            actualizada.estadoLimpieza === 'LIMPIA'
              ? habitaciones.filter((item) => item.numero !== actualizada.numero)
              : habitaciones.map((item) =>
                  item.numero === actualizada.numero ? actualizada : item
                )
          );
          this.mensaje.set(
            actualizada.estadoLimpieza === 'LIMPIA'
              ? `Habitación ${actualizada.numero} lista.`
              : `Limpieza de la habitación ${actualizada.numero} iniciada.`
          );
          this.actualizando.set(null);
        },
        error: (error: ApiError) => {
          this.error.set(error.mensaje);
          this.actualizando.set(null);
        },
      });
  }

  mostrarEstado(estado: EstadoLimpieza): string {
    return estado === 'EN_LIMPIEZA' ? 'En limpieza' : 'Sucia';
  }
}
