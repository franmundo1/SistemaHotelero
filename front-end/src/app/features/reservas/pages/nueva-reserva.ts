import { CurrencyPipe } from '@angular/common';
import { Component, computed, inject, input, OnInit, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { SesionService } from '../../../core/auth/sesion.service';
import { ApiError } from '../../../core/models/api-error';
import { Habitacion } from '../../habitaciones/models/habitacion';
import { HabitacionesService } from '../../habitaciones/services/habitaciones';
import { Huesped } from '../../huespedes/models/huesped';
import { HuespedesService } from '../../huespedes/services/huespedes';
import { CanalReserva, METODOS_PAGO, MetodoPago, Reserva } from '../models/reserva';
import { CanalesReservaService } from '../services/canales-reserva';
import { ReservasService } from '../services/reservas';

@Component({
  selector: 'app-nueva-reserva',
  imports: [ReactiveFormsModule, CurrencyPipe, RouterLink],
  templateUrl: './nueva-reserva.html',
  styleUrl: './nueva-reserva.css',
})
export class NuevaReserva implements OnInit {
  private readonly reservasService = inject(ReservasService);
  private readonly canalesService = inject(CanalesReservaService);
  private readonly habitacionesService = inject(HabitacionesService);
  private readonly huespedesService = inject(HuespedesService);
  private readonly sesionService = inject(SesionService);

  // Llegan como query params desde la pantalla de disponibilidad (H11)
  readonly habitacion = input<string>();
  readonly fechaEntrada = input<string>();
  readonly fechaSalida = input<string>();
  readonly personas = input<string>();

  protected readonly metodosPago = METODOS_PAGO;

  readonly habitacionElegida = signal<Habitacion | null>(null);
  readonly huespedes = signal<Huesped[]>([]);
  readonly canales = signal<CanalReserva[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  readonly guardando = signal(false);
  readonly errorGuardar = signal<string | null>(null);
  readonly reservaCreada = signal<Reserva | null>(null);

  readonly form = new FormGroup({
    huesped: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    canal: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    personas: new FormControl(1, { nonNullable: true, validators: [Validators.required, Validators.min(1)] }),
    anticipo: new FormControl(0, { nonNullable: true, validators: [Validators.required, Validators.min(0.01)] }),
    metodoPago: new FormControl<MetodoPago>('EFECTIVO', { nonNullable: true, validators: [Validators.required] }),
  });

  // Valores del form como signals para usarlos en los computed
  private readonly canalElegido = toSignal(this.form.controls.canal.valueChanges, { initialValue: '' });
  private readonly personasElegidas = toSignal(this.form.controls.personas.valueChanges, { initialValue: 1 });
  private readonly anticipoIngresado = toSignal(this.form.controls.anticipo.valueChanges, { initialValue: 0 });

  // Las fechas vienen en yyyy-mm-dd: la resta da milisegundos -> se pasa a dias
  readonly noches = computed(() => {
    const entrada = new Date(this.fechaEntrada() ?? '');
    const salida = new Date(this.fechaSalida() ?? '');
    return Math.round((salida.getTime() - entrada.getTime()) / (1000 * 60 * 60 * 24));
  });

  // Misma regla que el back: por BOOKING el precio por noche tiene un 20% de recargo
  readonly precioPorNoche = computed(() => {
    const precio = this.habitacionElegida()?.precioPorNoche ?? 0;
    const canal = this.canales().find((c) => c.idExterno === this.canalElegido());
    const precioFinal = canal?.tipo === 'BOOKING' ? precio * 1.2 : precio;
    return Math.round(precioFinal * 100) / 100; // redondeo a 2 decimales (centavos)
  });

  readonly total = computed(() => this.precioPorNoche() * this.noches());

  readonly superaCapacidad = computed(
    () => this.personasElegidas() > (this.habitacionElegida()?.capacidad ?? 0)
  );

  readonly anticipoSuperaTotal = computed(() => this.anticipoIngresado() > this.total());

  ngOnInit(): void {
    const idHabitacion = this.habitacion();
    if (!idHabitacion || !this.fechaEntrada() || !this.fechaSalida()) {
      this.error.set('Primero elegí una habitación y las fechas desde la consulta de disponibilidad.');
      this.cargando.set(false);
      return;
    }

    // Si no viene la cantidad de personas, se arranca con 1
    this.form.controls.personas.setValue(Number(this.personas()) || 1);

    this.habitacionesService.getById(idHabitacion).subscribe({
      next: (habitacion) => {
        this.habitacionElegida.set(habitacion);
        this.cargando.set(false);
      },
      error: (error: ApiError) => {
        this.error.set(error.mensaje);
        this.cargando.set(false);
      },
    });

    this.huespedesService.getAll().subscribe({
      next: (huespedes) => this.huespedes.set(huespedes),
      error: (error: ApiError) => this.error.set(error.mensaje),
    });

    this.canalesService.getAll().subscribe({
      next: (canales) => this.canales.set(canales),
      error: (error: ApiError) => this.error.set(error.mensaje),
    });
  }

  onConfirmar(): void {
    const habitacion = this.habitacionElegida();
    if (this.form.invalid || !habitacion) {
      this.form.markAllAsTouched();
      return;
    }
    if (this.superaCapacidad() || this.anticipoSuperaTotal()) {
      return;
    }

    const datos = this.form.getRawValue();
    this.guardando.set(true);
    this.errorGuardar.set(null);

    this.reservasService
      .create({
        idHuespedExterno: datos.huesped,
        numeroHabitacion: habitacion.numero,
        emailUsuarioCreador: this.sesionService.emailUsuario() ?? '', // usuario logueado
        idCanalReservaExterno: datos.canal,
        fechaEntrada: this.fechaEntrada() ?? '',
        fechaSalida: this.fechaSalida() ?? '',
        cantidadPersonas: datos.personas,
        anticipo: datos.anticipo,
        metodoPagoAnticipo: datos.metodoPago,
      })
      .subscribe({
        next: (reserva) => {
          this.reservaCreada.set(reserva);
          this.guardando.set(false);
        },
        // Ej: la habitacion ya tiene una reserva en esas fechas -> se muestra el mensaje de la API
        error: (error: ApiError) => {
          this.errorGuardar.set(error.mensaje);
          this.guardando.set(false);
        },
      });
  }

  mostrar(valor: string): string {
    const texto = valor.replaceAll('_', ' ').toLowerCase();
    return texto.charAt(0).toUpperCase() + texto.slice(1);
  }
}
