import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { ApiError } from '../../../core/models/api-error';
import { Reserva } from '../../reservas/models/reserva';
import { ReservasService } from '../../reservas/services/reservas';
import {
  Consumo,
  ConsumoLoteRequest,
  ItemCarritoConsumo,
  Producto,
} from '../models/consumo';
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
  readonly carrito = signal<ItemCarritoConsumo[]>([]);
  readonly cargando = signal(false);
  readonly cargandoConsumos = signal(false);
  readonly guardando = signal(false);
  readonly error = signal<string | null>(null);
  readonly errorRegistro = signal<string | null>(null);
  readonly mensaje = signal<string | null>(null);

  readonly totalConsumos = computed(() =>
    this.consumos().reduce((total, consumo) => total + consumo.subtotal, 0)
  );
  readonly totalCarrito = computed(() =>
  this.carrito().reduce(
    (total, item) => total + item.subtotal,
    0
  )
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
    this.carrito.set([]);
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

  agregarAlCarrito(): void {
  if (this.formulario.invalid) {
    this.formulario.markAllAsTouched();
    return;
  }

  const valores = this.formulario.getRawValue();
  const producto = this.productoSeleccionado();

  if (!producto) {
    this.errorRegistro.set('Seleccioná un producto.');
    return;
  }

  const itemExistente = this.carrito().find(
    (item) => item.idProductoExterno === producto.idExterno
  );

  const cantidadAnterior = itemExistente?.cantidad ?? 0;
  const cantidadTotal = cantidadAnterior + valores.cantidad;

  if (cantidadTotal > producto.stock) {
    this.errorRegistro.set(
      `Stock insuficiente. Solo hay ${producto.stock} unidades de ${producto.nombre}.`
    );
    return;
  }

  const observaciones = valores.observaciones.trim();

  this.carrito.update((items) => {
    if (itemExistente) {
      return items.map((item) =>
        item.idProductoExterno === producto.idExterno
          ? {
              ...item,
              cantidad: cantidadTotal,
              subtotal: cantidadTotal * producto.precio,
              observaciones:
                observaciones || item.observaciones,
            }
          : item
      );
    }

    return [
      ...items,
      {
        idProductoExterno: producto.idExterno,
        producto: producto.nombre,
        cantidad: valores.cantidad,
        precioUnitario: producto.precio,
        subtotal: valores.cantidad * producto.precio,
        observaciones: observaciones || undefined,
      },
    ];
  });

  this.formulario.patchValue({
    idProductoExterno: '',
    cantidad: 1,
    observaciones: '',
  });

  this.errorRegistro.set(null);
  this.mensaje.set(`${producto.nombre} agregado al carrito.`);
}

eliminarDelCarrito(idProductoExterno: string): void {
  this.carrito.update((items) =>
    items.filter(
      (item) => item.idProductoExterno !== idProductoExterno
    )
  );

  this.mensaje.set(null);
  this.errorRegistro.set(null);
}

vaciarCarrito(): void {
  this.carrito.set([]);
  this.mensaje.set(null);
  this.errorRegistro.set(null);
}

registrarCarrito(): void {
  const idReserva =
    this.formulario.controls.idReservaExterno.value;

  if (!idReserva) {
    this.errorRegistro.set('Seleccioná una reserva.');
    return;
  }

  if (this.carrito().length === 0) {
    this.errorRegistro.set(
      'Agregá al menos un producto al carrito.'
    );
    return;
  }

  const datos: ConsumoLoteRequest = {
    idReservaExterno: idReserva,
    consumos: this.carrito().map((item) => ({
      idProductoExterno: item.idProductoExterno,
      cantidad: item.cantidad,
      ...(item.observaciones
        ? { observaciones: item.observaciones }
        : {}),
    })),
  };

  this.guardando.set(true);
  this.errorRegistro.set(null);
  this.mensaje.set(null);

  this.consumosService.registrarLote(datos).subscribe({
    next: (consumosRegistrados) => {
      this.consumos.update((consumosActuales) => [
        ...consumosActuales,
        ...consumosRegistrados,
      ]);

      this.productos.update((productos) =>
        productos.map((producto) => {
          const cantidadRegistrada = consumosRegistrados
            .filter(
              (consumo) =>
                consumo.idProductoExterno === producto.idExterno
            )
            .reduce(
              (total, consumo) => total + consumo.cantidad,
              0
            );

          return cantidadRegistrada > 0
            ? {
                ...producto,
                stock: producto.stock - cantidadRegistrada,
              }
            : producto;
        })
      );

      const cantidad = consumosRegistrados.length;

      this.carrito.set([]);
      this.guardando.set(false);
      this.mensaje.set(
        cantidad === 1
          ? 'Consumo registrado correctamente.'
          : `${cantidad} consumos registrados correctamente.`
      );
    },
    error: (error: ApiError) => {
      this.errorRegistro.set(error.mensaje);
      this.guardando.set(false);
    },
  });
}
}
