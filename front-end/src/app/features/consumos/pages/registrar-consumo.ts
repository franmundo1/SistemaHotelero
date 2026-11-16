import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { ApiError } from '../../../core/models/api-error';
import { Reserva } from '../../reservas/models/reserva';
import { ReservasService } from '../../reservas/services/reservas';
import { Consumo, ConsumoRequest, Producto } from '../models/consumo';
import { ConsumosService } from '../services/consumos';
import { ProductosService } from '../services/productos';

@Component({
  selector: 'app-registrar-consumo',
  imports: [ReactiveFormsModule, CurrencyPipe, DatePipe],
  templateUrl: './registrar-consumo.html',
  styleUrl: './registrar-consumo.css',
})
export class RegistrarConsumo implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  private readonly reservasService = inject(ReservasService);
  private readonly productosService = inject(ProductosService);
  private readonly consumosService = inject(ConsumosService);

  readonly reservas = signal<Reserva[]>([]);
  readonly productos = signal<Producto[]>([]);
  readonly consumos = signal<Consumo[]>([]);
  readonly cargando = signal(false);
  readonly cargandoConsumos = signal(false);
  readonly guardando = signal(false);
  readonly error = signal<string | null>(null);
  readonly errorRegistro = signal<string | null>(null);
  readonly mensaje = signal<string | null>(null);

  readonly totalConsumos = computed(() =>
    this.consumos().reduce((total, consumo) => total + consumo.subtotal, 0)
  );

  readonly formulario = this.formBuilder.nonNullable.group({
    idReservaExterno: ['', Validators.required],
    idProductoExterno: ['', Validators.required],
    cantidad: [1, [Validators.required, Validators.min(1)]],
    observaciones: [''],
  });

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.cargando.set(true);
    this.error.set(null);

    forkJoin({
      reservas: this.reservasService.listarEnCurso(),
      productos: this.productosService.listar(),
    }).subscribe({
      next: ({ reservas, productos }) => {
        this.reservas.set(reservas);
        this.productos.set(productos.filter((producto) => producto.activo));
        this.cargando.set(false);
      },
      error: (error: ApiError) => {
        this.error.set(error.mensaje);
        this.cargando.set(false);
      },
    });
  }

  cargarConsumos(): void {
    const idReserva = this.formulario.controls.idReservaExterno.value;

    this.consumos.set([]);
    this.errorRegistro.set(null);
    this.mensaje.set(null);

    if (!idReserva) {
      return;
    }

    this.cargandoConsumos.set(true);

    this.consumosService.listarPorReserva(idReserva).subscribe({
      next: (consumos) => {
        this.consumos.set(consumos);
        this.cargandoConsumos.set(false);
      },
      error: (error: ApiError) => {
        this.errorRegistro.set(error.mensaje);
        this.cargandoConsumos.set(false);
      },
    });
  }

  productoSeleccionado(): Producto | undefined {
    const idProducto = this.formulario.controls.idProductoExterno.value;
    return this.productos().find((producto) => producto.idExterno === idProducto);
  }

  registrar(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    const valores = this.formulario.getRawValue();
    const datos: ConsumoRequest = {
      idReservaExterno: valores.idReservaExterno,
      idProductoExterno: valores.idProductoExterno,
      cantidad: valores.cantidad,
    };

    const observaciones = valores.observaciones.trim();
    if (observaciones) {
      datos.observaciones = observaciones;
    }

    this.guardando.set(true);
    this.errorRegistro.set(null);
    this.mensaje.set(null);

    this.consumosService.registrar(datos).subscribe({
      next: (consumo) => {
        this.consumos.update((consumos) => [...consumos, consumo]);
        this.productos.update((productos) =>
          productos.map((producto) =>
            producto.idExterno === consumo.idProductoExterno
              ? { ...producto, stock: producto.stock - consumo.cantidad }
              : producto
          )
        );

        this.formulario.patchValue({
          idProductoExterno: '',
          cantidad: 1,
          observaciones: '',
        });
        this.mensaje.set('Consumo registrado correctamente.');
        this.guardando.set(false);
      },
      error: (error: ApiError) => {
        this.errorRegistro.set(error.mensaje);
        this.guardando.set(false);
      },
    });
  }
}
