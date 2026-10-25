import { CurrencyPipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { ApiError } from '../../../core/models/api-error';
import { FormularioHabitacion } from '../components/formulario-habitacion/formulario-habitacion';
import { Habitacion, HabitacionRequest } from '../models/habitacion';
import { HabitacionesService } from '../services/habitaciones';

@Component({
  selector: 'app-listado-habitaciones',
  imports: [CurrencyPipe, FormularioHabitacion],
  templateUrl: './listado-habitaciones.html',
  styleUrl: './listado-habitaciones.css',
})
export class ListadoHabitaciones implements OnInit {
  private readonly habitacionesService = inject(HabitacionesService);

  readonly habitaciones = signal<Habitacion[]>([]);
  readonly cargando = signal(false);
  readonly error = signal<string | null>(null);

  readonly formularioAbierto = signal(false);
  readonly habitacionEnEdicion = signal<Habitacion | null>(null);
  readonly guardando = signal(false);
  readonly errorFormulario = signal<string | null>(null);
  readonly mensaje = signal<string | null>(null);
  readonly errorBaja = signal<string | null>(null);

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

  onNueva(): void {
    this.abrirFormulario(null);
  }

  onEditar(habitacion: Habitacion): void {
    this.abrirFormulario(habitacion);
  }

  onCancelar(): void {
    this.formularioAbierto.set(false);
    this.habitacionEnEdicion.set(null);
  }

  onGuardar(datos: HabitacionRequest): void {
    const original = this.habitacionEnEdicion();
    const peticion = original
      ? this.habitacionesService.update(original.idExterno, this.cambios(original, datos))
      : this.habitacionesService.create(datos);

    this.guardando.set(true);
    this.errorFormulario.set(null);

    peticion.subscribe({
      next: (habitacion) => {
        this.guardando.set(false);
        this.onCancelar();
        this.mensaje.set(`Habitación ${habitacion.numero} ${original ? 'actualizada' : 'creada'} correctamente.`);
        this.cargarHabitaciones();
      },
      error: (error: ApiError) => {
        this.guardando.set(false);
        this.errorFormulario.set(error.mensaje);
      },
    });
  }

  onDarDeBaja(habitacion: Habitacion): void {
    const confirmado = confirm(
      `¿Seguro que queres dar de baja la habitación ${habitacion.numero}? ` +
        'Pasará a estado "Inactiva" y no se ofrecerá en nuevas reservas.'
    );
    if (!confirmado) {
      return;
    }

    this.mensaje.set(null);
    this.errorBaja.set(null);

    this.habitacionesService.delete(habitacion.idExterno).subscribe({
      next: () => {
        // El listado solo trae activas: la sacamos sin recargar
        this.habitaciones.update((lista) => lista.filter((h) => h.idExterno !== habitacion.idExterno));
        if (this.habitacionEnEdicion()?.idExterno === habitacion.idExterno) {
          this.onCancelar();
        }
        this.mensaje.set(`Habitación ${habitacion.numero} dada de baja.`);
      },
      error: (error: ApiError) => this.errorBaja.set(error.mensaje),
    });
  }

  mostrarEstado(estado: string): string {
    const texto = estado.replaceAll('_', ' ').toLowerCase();

    return texto.charAt(0).toUpperCase() + texto.slice(1);
  }

  private abrirFormulario(habitacion: Habitacion | null): void {
    this.mensaje.set(null);
    this.errorBaja.set(null);
    this.errorFormulario.set(null);
    this.habitacionEnEdicion.set(habitacion);
    this.formularioAbierto.set(true);
  }

  // Solo los campos modificados (si el numero no cambia no se envia)
  private cambios(original: Habitacion, datos: HabitacionRequest): Partial<HabitacionRequest> {
    const cambios: Record<string, unknown> = {};
    for (const [clave, valor] of Object.entries(datos)) {
      if (valor !== original[clave as keyof Habitacion]) {
        cambios[clave] = valor;
      }
    }
    return cambios;
  }
}
