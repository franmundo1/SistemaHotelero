import { Component, input, output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ROLES_EMPLEADO, RolEmpleado, UsuarioRequest } from '../../models/usuario';

@Component({
  selector: 'app-formulario-usuario',
  imports: [ReactiveFormsModule],
  templateUrl: './formulario-usuario.html',
  styleUrl: './formulario-usuario.css',
})
export class FormularioUsuario {
  readonly loading = input(false);
  readonly error = input<string | null>(null);
  readonly guardar = output<UsuarioRequest>(); // evento que sale hacia el padre
  readonly cancelar = output<void>();

  protected readonly roles = ROLES_EMPLEADO;

  // Mismas reglas que UsuarioRequestDTO del back
  readonly form = new FormGroup({
    nombre: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    apellido: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.email],
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(6)],
    }),
    rol: new FormControl<RolEmpleado>('ROLE_RECEPCIONISTA', {
      nonNullable: true,
      validators: [Validators.required],
    }),
  });

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.guardar.emit(this.form.getRawValue());
  }

  onCancelar(): void {
    this.cancelar.emit();
  }
}
