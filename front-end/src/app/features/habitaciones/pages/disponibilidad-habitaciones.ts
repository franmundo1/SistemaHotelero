import { CurrencyPipe } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { ApiError } from '../../../core/models/api-error';
import { Habitacion } from '../models/habitacion';
import { HabitacionesService } from '../services/habitaciones';

interface Busqueda {
  fechaEntrada: string;
  fechaSalida: string;
  personas: number;
}

// Fecha de hoy en formato yyyy-mm-dd (el que usa el input date)
function fechaDeHoy(): string {
  return new Date().toLocaleDateString('en-CA');
}

// La fecha de entrada no puede ser anterior a hoy (yyyy-mm-dd se compara como texto)
const fechaNoPasada: ValidatorFn = (control: AbstractControl): ValidationErrors | null =>
  control.value && control.value < fechaDeHoy() ? { fechaPasada: true } : null;

// Validador de grupo: la salida tiene que ser posterior a la entrada
const rangoFechas: ValidatorFn = (grupo: AbstractControl): ValidationErrors | null => {
  const entrada = grupo.get('fechaEntrada')?.value;
  const salida = grupo.get('fechaSalida')?.value;
  return entrada && salida && salida <= entrada ? { rangoInvalido: true } : null;
};



@Component({
  selector: 'app-disponibilidad-habitaciones',
  imports: [ReactiveFormsModule, CurrencyPipe],
  templateUrl: './disponibilidad-habitaciones.html',
  styleUrl: './disponibilidad-habitaciones.css',
})
export class DisponibilidadHabitaciones {
  private readonly habitacionesService = inject(HabitacionesService);
  private readonly router = inject(Router);

  protected readonly hoy = fechaDeHoy();

  readonly habitaciones = signal<Habitacion[]>([]);
  readonly buscando = signal(false);
  readonly error = signal<string | null>(null);
  readonly busqueda = signal<Busqueda | null>(null); // null = todavia no se busco
  readonly numeroBuscado = signal('');

  readonly form = new FormGroup({
    fechaEntrada: new FormControl('', { nonNullable: true, validators: [Validators.required, fechaNoPasada] }),
    fechaSalida: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    personas: new FormControl(1, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(1), Validators.max(3)],
    }),
  }, { validators: rangoFechas });

  // El endpoint no recibe la capacidad: se filtra aca
  readonly disponibles = computed(() => {
    const personas = this.busqueda()?.personas ?? 1;
    return this.habitaciones().filter((h) => h.capacidad >= personas);
  });

  readonly disponiblesFiltradas = computed(() => {
  const numero = this.numeroBuscado().trim().toLowerCase();

  if (!numero) {
    return this.disponibles();
  }

  return this.disponibles().filter((habitacion) =>
    habitacion.numero.toLowerCase().includes(numero)
  );
});

  onBuscar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const datos = this.form.getRawValue();

    this.buscando.set(true);
    this.error.set(null);
    this.numeroBuscado.set('');

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

  onBuscarNumero(event: Event): void {
  const input = event.target as HTMLInputElement;

  this.numeroBuscado.set(input.value);
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
