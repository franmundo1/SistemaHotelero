import { CurrencyPipe } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiError } from '../../../core/models/api-error';
import { Habitacion } from '../models/habitacion';
import { HabitacionesService } from '../services/habitaciones';

interface Busqueda {
  fechaEntrada: string;
  fechaSalida: string;
  personas: number;
}

@Component({
  selector: 'app-disponibilidad-habitaciones',
  imports: [ReactiveFormsModule, CurrencyPipe],
  templateUrl: './disponibilidad-habitaciones.html',
  styleUrl: './disponibilidad-habitaciones.css',
})
export class DisponibilidadHabitaciones {
  private readonly habitacionesService = inject(HabitacionesService);
  private readonly router = inject(Router);

  // Fecha de hoy en formato yyyy-mm-dd (el que usa el input date)
  protected readonly hoy = new Date().toLocaleDateString('en-CA');

  readonly habitaciones = signal<Habitacion[]>([]);
  readonly buscando = signal(false);
  readonly error = signal<string | null>(null);
  readonly busqueda = signal<Busqueda | null>(null); // null = todavia no se busco

  readonly form = new FormGroup({
    fechaEntrada: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    fechaSalida: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    personas: new FormControl(1, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(1), Validators.max(3)],
    }),
  });

  // El endpoint no recibe la capacidad: se filtra aca
  readonly disponibles = computed(() => {
    const personas = this.busqueda()?.personas ?? 1;
    return this.habitaciones().filter((h) => h.capacidad >= personas);
  });

  onBuscar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const datos = this.form.getRawValue();
    // Las fechas en yyyy-mm-dd se pueden comparar como texto
    if (datos.fechaEntrada < this.hoy) {
      this.error.set('La fecha de entrada no puede ser pasada.');
      return;
    }
    if (datos.fechaSalida <= datos.fechaEntrada) {
      this.error.set('La fecha de salida debe ser posterior a la fecha de entrada.');
      return;
    }

    this.buscando.set(true);
    this.error.set(null);

    this.habitacionesService.getDisponibles(datos.fechaEntrada, datos.fechaSalida).subscribe({
      next: (habitaciones) => {
        this.habitaciones.set(habitaciones);
        this.busqueda.set(datos);
        this.buscando.set(false);
      },
      error: (error: ApiError) => {
        this.error.set(error.mensaje);
        this.busqueda.set(null);
        this.buscando.set(false);
      },
    });
  }

  // Pasa a la reserva (H9) con la habitacion y las fechas ya elegidas
  onReservar(habitacion: Habitacion): void {
    const busqueda = this.busqueda();
    if (!busqueda) {
      return;
    }
    this.router.navigate(['/reservas/nueva'], {
      queryParams: { habitacion: habitacion.idExterno, ...busqueda },
    });
  }

  mostrar(valor: string): string {
    const texto = valor.replaceAll('_', ' ').toLowerCase();
    return texto.charAt(0).toUpperCase() + texto.slice(1);
  }
}
