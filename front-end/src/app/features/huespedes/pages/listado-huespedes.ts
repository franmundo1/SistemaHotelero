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
    const confirmado = confirm(
      `¿Seguro que querés eliminar a ${huesped.nombre} ${huesped.apellido}? ` +
        'Se borrarán sus datos y no se puede deshacer.'
    );
    if (!confirmado) {
      return;
    }

    this.mensaje.set(null);
    this.errorBaja.set(null);

    this.huespedesService.delete(huesped.idExterno).subscribe({
      next: () => {
        // Lo sacamos de la lista sin recargar
        this.huespedes.update((lista) => lista.filter((h) => h.idExterno !== huesped.idExterno));
        if (this.huespedEnEdicion()?.idExterno === huesped.idExterno) {
          this.onCancelar();
        }
        this.mensaje.set(`Huésped ${huesped.nombre} ${huesped.apellido} eliminado.`);
      },
      error: (error: ApiError) => {
        // 409: la base no deja borrarlo porque tiene reservas asociadas
        this.errorBaja.set(
          error.status === 409
            ? `No se puede eliminar a ${huesped.nombre} ${huesped.apellido} porque tiene reservas asociadas.`
            : error.mensaje
        );
      },
    });
  }

  private abrirFormulario(huesped: Huesped | null): void {
    this.mensaje.set(null);
    this.errorBaja.set(null);
    this.errorFormulario.set(null);
    this.huespedEnEdicion.set(huesped);
    this.formularioAbierto.set(true);
  }
}
