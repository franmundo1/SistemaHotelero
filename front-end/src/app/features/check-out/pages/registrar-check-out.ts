import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiError } from '../../../core/models/api-error';
import { METODOS_PAGO } from '../../reservas/models/reserva';
import { Reserva } from '../../reservas/models/reserva';
import { ReservasService } from '../../reservas/services/reservas';
import {
  CheckOutRequest,
  CheckOutResponse,
  CheckOutResumen,
} from '../models/check-out';
import { CheckOutService } from '../services/check-out';

@Component({
  selector: 'app-registrar-check-out',
  imports: [ReactiveFormsModule, CurrencyPipe, DatePipe],
  templateUrl: './registrar-check-out.html',
  styleUrl: './registrar-check-out.css',
})
export class RegistrarCheckOut implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  private readonly reservasService = inject(ReservasService);
  private readonly checkOutService = inject(CheckOutService);

  readonly reservas = signal<Reserva[]>([]);
  readonly resumen = signal<CheckOutResumen | null>(null);
  readonly resultado = signal<CheckOutResponse | null>(null);
  readonly cargando = signal(false);
  readonly cargandoResumen = signal(false);
  readonly guardando = signal(false);
  readonly error = signal<string | null>(null);
  readonly metodosPago = METODOS_PAGO;

  readonly formulario = this.formBuilder.nonNullable.group({
    idReservaExterno: ['', Validators.required],
    montoAbonado: [0, [Validators.required, Validators.min(0.01)]],
    metodoPago: ['EFECTIVO', Validators.required],
    observaciones: [''],
  });

  ngOnInit(): void {
    this.cargarReservas();
  }

  cargarReservas(): void {
    this.cargando.set(true);
    this.error.set(null);

    this.reservasService.listarEnCurso().subscribe({
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

  cargarResumen(): void {
    const idReserva = this.formulario.controls.idReservaExterno.value;

    this.resumen.set(null);
    this.resultado.set(null);
    this.error.set(null);

    if (!idReserva) {
      return;
    }

    this.cargandoResumen.set(true);

    this.checkOutService.obtenerResumen(idReserva).subscribe({
      next: (resumen) => {
        this.resumen.set(resumen);
        this.formulario.controls.montoAbonado.setValue(resumen.saldoPendiente);
        this.cargandoResumen.set(false);
      },
      error: (error: ApiError) => {
        this.error.set(error.mensaje);
        this.cargandoResumen.set(false);
      },
    });
  }

  registrar(): void {
    if (this.formulario.invalid || this.resumen() === null) {
      this.formulario.markAllAsTouched();
      return;
    }

    const valores = this.formulario.getRawValue();
    const datos: CheckOutRequest = {
      idReservaExterno: valores.idReservaExterno,
      montoAbonado: valores.montoAbonado,
      metodoPago: valores.metodoPago,
    };

    const observaciones = valores.observaciones.trim();
    if (observaciones) {
      datos.observaciones = observaciones;
    }

    this.guardando.set(true);
    this.error.set(null);

    this.checkOutService.registrar(datos).subscribe({
      next: (checkOut) => {
        this.resultado.set(checkOut);
        this.reservas.update((reservas) =>
          reservas.filter(
            (reserva) => reserva.idExterno !== checkOut.idReservaExterno
          )
        );
        this.resumen.set(null);
        this.formulario.reset({
          idReservaExterno: '',
          montoAbonado: 0,
          metodoPago: 'EFECTIVO',
          observaciones: '',
        });
        this.guardando.set(false);
      },
      error: (error: ApiError) => {
        this.error.set(error.mensaje);
        this.guardando.set(false);
      },
    });
  }

  mostrarMetodo(metodo: string): string {
    return metodo.replaceAll('_', ' ');
  }
}
