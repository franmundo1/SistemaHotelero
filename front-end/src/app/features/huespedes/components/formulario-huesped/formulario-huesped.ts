import { Component, input, OnInit, output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Huesped, HuespedRequest } from '../../models/huesped';

@Component({
  selector: 'app-formulario-huesped',
  imports: [ReactiveFormsModule],
  templateUrl: './formulario-huesped.html',
  styleUrl: './formulario-huesped.css',
})
export class FormularioHuesped implements OnInit {
  readonly huesped = input<Huesped | null>(null); // null = alta, con valor = edicion
  readonly loading = input(false);
  readonly error = input<string | null>(null);
  readonly guardar = output<HuespedRequest>(); // evento que sale hacia el padre
  readonly cancelar = output<void>();

  // Mismas reglas que HuespedRequestDTO del back
  readonly form = new FormGroup({
    nombre: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(2), Validators.maxLength(50)],
    }),
    apellido: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(2), Validators.maxLength(50)],
    }),
    dni: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(/^[0-9]{7,10}$/)],
    }),
    telefono: new FormControl('', {
      nonNullable: true,
      validators: [Validators.pattern(/^\+?[0-9]{10,15}$/)],
    }),
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.email],
    }),
  });

  ngOnInit(): void {
    const huesped = this.huesped();
    if (huesped) {
      const { idExterno, telefono, ...datos } = huesped;
      this.form.setValue({ ...datos, telefono: telefono ?? '' });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const { telefono, ...datos } = this.form.getRawValue();
    // El telefono es opcional: si esta vacio se manda null
    this.guardar.emit({ ...datos, telefono: telefono || null });
  }

  onCancelar(): void {
    this.cancelar.emit();
  }
}
