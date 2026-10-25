import { Component, input, OnInit, output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {
  EstadoLimpieza,
  EstadoOcupacion,
  Habitacion,
  HabitacionRequest,
  TipoHabitacion,
} from '../../models/habitacion';

@Component({
  selector: 'app-formulario-habitacion',
  imports: [ReactiveFormsModule],
  templateUrl: './formulario-habitacion.html',
  styleUrl: './formulario-habitacion.css',
})
export class FormularioHabitacion implements OnInit {
  readonly habitacion = input<Habitacion | null>(null); // null = alta, con valor = edicion
  readonly loading = input(false);
  readonly error = input<string | null>(null);
  readonly guardar = output<HabitacionRequest>(); // evento que sale hacia el padre
  readonly cancelar = output<void>();

  protected readonly tipos: TipoHabitacion[] = ['SIMPLE', 'DOBLE', 'SUITE'];
  protected readonly estadosOcupacion: EstadoOcupacion[] = ['DISPONIBLE', 'OCUPADA', 'MANTENIMIENTO'];
  protected readonly estadosLimpieza: EstadoLimpieza[] = ['LIMPIA', 'SUCIA', 'EN_LIMPIEZA'];

  // Mismas reglas que HabitacionRequestDTO del back
  readonly form = new FormGroup({
    numero: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(/^[1-9][0-9]*$/)],
    }),
    tipo: new FormControl<TipoHabitacion>('SIMPLE', { nonNullable: true, validators: [Validators.required] }),
    capacidad: new FormControl(1, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(1), Validators.max(3)],
    }),
    precioPorNoche: new FormControl(0, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(0.01)],
    }),
    estadoOcupacion: new FormControl<EstadoOcupacion>('DISPONIBLE', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    estadoLimpieza: new FormControl<EstadoLimpieza>('LIMPIA', {
      nonNullable: true,
      validators: [Validators.required],
    }),
  });

  ngOnInit(): void {
    const habitacion = this.habitacion();
    if (habitacion) {
      const { idExterno, activa, ...datos } = habitacion;
      this.form.setValue(datos);
    }
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.guardar.emit({ ...this.form.getRawValue(), activa: true });
  }

  onCancelar(): void {
    this.cancelar.emit();
  }

  mostrar(valor: string): string {
    const texto = valor.replaceAll('_', ' ').toLowerCase();
    return texto.charAt(0).toUpperCase() + texto.slice(1);
  }
}
