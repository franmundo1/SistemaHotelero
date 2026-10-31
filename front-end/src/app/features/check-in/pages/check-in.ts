import { DatePipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ApiError } from '../../../core/models/api-error';
import { Reserva } from '../../reservas/models/reserva';
import { ReservasService } from '../../reservas/services/reservas';
import { CheckIn, CheckInRequest } from '../models/check-in';
import { CheckInService } from '../services/check-in';

@Component({
  selector: 'app-registrar-check-in',
  imports: [ReactiveFormsModule, DatePipe],
  templateUrl: './check-in.html',
  styleUrl: './check-in.css',
})
export class RegistrarCheckIn implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  private readonly reservasService = inject(ReservasService);
  private readonly checkInService = inject(CheckInService);

  readonly reservas = signal<Reserva[]>([]);
  readonly cargando = signal(false);
  readonly guardando = signal(false);
  readonly error = signal<string | null>(null);
  readonly errorRegistro = signal<string | null>(null);
  readonly checkInRegistrado = signal<CheckIn | null>(null);

  readonly fechaHoy = this.obtenerFechaLocal();

  readonly formulario = this.formBuilder.nonNullable.group({
    idReservaExterno: ['', Validators.required],
    observaciones: [''],
  });

  ngOnInit(): void {
    this.cargarReservas();
  }

  cargarReservas(): void {
    this.cargando.set(true);
    this.error.set(null);

    this.reservasService.listarConfirmadas().subscribe({
      next: (reservas) => {
        const reservasDeHoy = reservas.filter(
          (reserva) => reserva.fechaEntrada === this.fechaHoy
        );

        this.reservas.set(reservasDeHoy);
        this.cargando.set(false);
      },
      error: (error: ApiError) => {
        this.error.set(error.mensaje);
        this.cargando.set(false);
      },
    });
  }

  reservaSeleccionada(): Reserva | undefined {
    const idReserva = this.formulario.controls.idReservaExterno.value;

    return this.reservas().find(
      (reserva) => reserva.idExterno === idReserva
    );
  }

  registrar(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    const valores = this.formulario.getRawValue();

    const datos: CheckInRequest = {
      idReservaExterno: valores.idReservaExterno,
    };

    const observaciones = valores.observaciones.trim();

    if (observaciones) {
      datos.observaciones = observaciones;
    }

    this.guardando.set(true);
    this.errorRegistro.set(null);
    this.checkInRegistrado.set(null);

    this.checkInService.registrar(datos).subscribe({
      next: (checkIn) => {
        this.checkInRegistrado.set(checkIn);

        this.reservas.update((reservas) =>
          reservas.filter(
            (reserva) => reserva.idExterno !== checkIn.idReservaExterno
          )
        );

        this.formulario.reset();
        this.guardando.set(false);
      },
      error: (error: ApiError) => {
        this.errorRegistro.set(error.mensaje);
        this.guardando.set(false);
      },
    });
  }

  private obtenerFechaLocal(): string {
    const hoy = new Date();
    const diferenciaZonaHoraria = hoy.getTimezoneOffset() * 60_000;

    return new Date(hoy.getTime() - diferenciaZonaHoraria)
      .toISOString()
      .slice(0, 10);
  }
}
