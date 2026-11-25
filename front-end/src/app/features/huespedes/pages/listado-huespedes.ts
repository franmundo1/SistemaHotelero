import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { ApiError } from '../../../core/models/api-error';
import { FormularioHuesped } from '../components/formulario-huesped/formulario-huesped';
import { Huesped, HuespedRequest } from '../models/huesped';
import { HuespedesService } from '../services/huespedes';

@Component({
  selector: 'app-listado-huespedes',
  imports: [ReactiveFormsModule, FormularioHuesped],
  templateUrl: './listado-huespedes.html',
  styleUrl: './listado-huespedes.css',
})
export class ListadoHuespedes implements OnInit {
  private readonly huespedesService = inject(HuespedesService);

  readonly huespedes = signal<Huesped[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  readonly formularioAbierto = signal(false);
  readonly huespedEnEdicion = signal<Huesped | null>(null);
  readonly guardando = signal(false);
  readonly errorFormulario = signal<string | null>(null);
  readonly mensaje = signal<string | null>(null);
  readonly errorBaja = signal<string | null>(null);
  readonly huespedAEliminar = signal<Huesped | null>(null);
  readonly eliminando = signal(false);

  // Buscador: el valor del control pasa a una signal para usarlo en el computed
  readonly buscador = new FormControl('', { nonNullable: true });
  private readonly textoBuscado = toSignal(this.buscador.valueChanges, { initialValue: '' });

  readonly huespedesFiltrados = computed(() => {
    const texto = this.textoBuscado().trim().toLowerCase();
    return this.huespedes().filter(
      (h) => `${h.nombre} ${h.apellido}`.toLowerCase().includes(texto) || h.dni.includes(texto)
    );
  });

  ngOnInit(): void {
    this.cargarHuespedes();
  }

  cargarHuespedes(): void {
    this.cargando.set(true);
    this.error.set(null);

    this.huespedesService.getAll().subscribe({
      next: (huespedes) => {
        this.huespedes.set(huespedes);
        this.cargando.set(false);
      },
      error: (error: ApiError) => {
        this.error.set(error.mensaje);
        this.cargando.set(false);
      },
    });
  }

  onNuevo(): void {
    this.abrirFormulario(null);
  }

  onEditar(huesped: Huesped): void {
    this.abrirFormulario(huesped);
  }

  onCancelar(): void {
    this.formularioAbierto.set(false);
    this.huespedEnEdicion.set(null);
  }

  onGuardar(datos: HuespedRequest): void {
    const original = this.huespedEnEdicion();
    const peticion = original
      ? this.huespedesService.update(original.idExterno, datos)
      : this.huespedesService.create(datos);

    this.guardando.set(true);
    this.errorFormulario.set(null);

    peticion.subscribe({
      next: (huesped) => {
        this.guardando.set(false);
        this.onCancelar();
        this.mensaje.set(
          `Huésped ${huesped.nombre} ${huesped.apellido} ${original ? 'actualizado' : 'registrado'} correctamente.`
        );
        this.cargarHuespedes();
      },
      error: (error: ApiError) => {
        this.guardando.set(false);
        this.errorFormulario.set(error.mensaje);
      },
    });
  }

  onEliminar(huesped: Huesped): void {
  this.mensaje.set(null);
  this.errorBaja.set(null);
  this.huespedAEliminar.set(huesped);
}

onCerrarEliminacion(): void {
  if (this.eliminando()) {
    return;
  }

  this.huespedAEliminar.set(null);
  this.errorBaja.set(null);
}

onConfirmarEliminacion(): void {
  const huesped = this.huespedAEliminar();

  if (!huesped) {
    return;
  }

  this.eliminando.set(true);
  this.errorBaja.set(null);
  this.mensaje.set(null);

  this.huespedesService.delete(huesped.idExterno).subscribe({
    next: (baja) => {
      this.huespedes.update((lista) =>
        lista.filter(
          (h) => h.idExterno !== huesped.idExterno
        )
      );

      if (
        this.huespedEnEdicion()?.idExterno ===
        huesped.idExterno
      ) {
        this.onCancelar();
      }

      this.eliminando.set(false);
      this.huespedAEliminar.set(null);

      this.mensaje.set(
        baja.tipo === 'LOGICA'
          ? `Huésped ${huesped.nombre} ${huesped.apellido} dado de baja. Se conserva su historial de reservas.`
          : `Huésped ${huesped.nombre} ${huesped.apellido} eliminado correctamente.`
      );
    },
    error: (error: ApiError) => {
      this.eliminando.set(false);

      this.errorBaja.set(
        error.status === 409
          ? `No se puede dar de baja a ${huesped.nombre} ${huesped.apellido} porque tiene una reserva confirmada o en curso.`
          : error.mensaje
      );
    },
  });
}

cerrarNotificacion(): void {
  this.mensaje.set(null);
}

  private abrirFormulario(huesped: Huesped | null): void {
    this.mensaje.set(null);
    this.errorBaja.set(null);
    this.errorFormulario.set(null);
    this.huespedEnEdicion.set(huesped);
    this.formularioAbierto.set(true);
  }
}
