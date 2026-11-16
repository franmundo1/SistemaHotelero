import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ApiError } from '../../../core/models/api-error';
import { Reserva } from '../models/reserva';
import { ReservasService } from '../services/reservas';

@Component({
  selector: 'app-listado-reservas',
  imports: [CurrencyPipe, DatePipe, ReactiveFormsModule, RouterLink],
  templateUrl: './listado-reservas.html',
  styleUrl: './listado-reservas.css',
})
export class ListadoReservas implements OnInit {
  private readonly reservasService = inject(ReservasService);

  readonly reservas = signal<Reserva[]>([]);
  readonly cargando = signal(false);
  readonly error = signal<string | null>(null);
  readonly mensaje = signal<string | null>(null);

  readonly reservaACancelar = signal<Reserva | null>(null);
  readonly cancelando = signal(false);
  readonly errorCancelacion = signal<string | null>(null);

  readonly formularioCancelacion = new FormGroup({
    motivoCancelacion: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
  });

  ngOnInit(): void {
    this.cargarReservas();
  }

  cargarReservas(): void {
    this.cargando.set(true);
    this.error.set(null);
    this.mensaje.set(null);

    this.reservasService.listar().subscribe({
      next: (reservas) => {
        this.reservas.set(reservas);
        this.cargando.set(false);
      },
      error: (error: ApiError) => {
        this.error.set(error.mensaje);
        this.cargando.set(false);
      },
    });
  }

  onAbrirCancelacion(reserva: Reserva): void {
    if (!this.puedeCancelar(reserva)) {
      return;
    }

    this.reservaACancelar.set(reserva);
    this.errorCancelacion.set(null);
    this.formularioCancelacion.reset();
  }

  onCerrarCancelacion(): void {
    if (this.cancelando()) {
      return;
    }

    this.reservaACancelar.set(null);
    this.errorCancelacion.set(null);
    this.formularioCancelacion.reset();
  }

  onConfirmarCancelacion(): void {
    const reserva = this.reservaACancelar();

    if (reserva === null) {
      return;
    }

    const motivo =
      this.formularioCancelacion.controls.motivoCancelacion.value.trim();

    if (!motivo) {
      this.formularioCancelacion.controls.motivoCancelacion.markAsTouched();
      this.errorCancelacion.set(
        'El motivo de cancelación es obligatorio.'
      );
      return;
    }

    this.cancelando.set(true);
    this.errorCancelacion.set(null);

    this.reservasService
      .cancelar(reserva.idExterno, motivo)
      .subscribe({
        next: (reservaCancelada) => {
          this.reservas.update((reservas) =>
            reservas.map((reservaActual) =>
              reservaActual.idExterno === reservaCancelada.idExterno
                ? reservaCancelada
                : reservaActual
            )
          );

          this.cancelando.set(false);
          this.reservaACancelar.set(null);
          this.formularioCancelacion.reset();
          this.mensaje.set(
            `La reserva de ${reservaCancelada.huesped} fue cancelada correctamente.`
          );
        },
        error: (error: ApiError) => {
          this.errorCancelacion.set(error.mensaje);
          this.cancelando.set(false);
        },
      });
  }

  puedeCancelar(reserva: Reserva): boolean {
    return reserva.estado.toUpperCase() === 'CONFIRMADA';
  }

  mostrarEstado(estado: string): string {
    const texto = estado.replaceAll('_', ' ').toLowerCase();

    return texto.charAt(0).toUpperCase() + texto.slice(1);
  }
}
