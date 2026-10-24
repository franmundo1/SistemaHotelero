import { Component, inject, signal } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { SesionService } from '../../../core/auth/sesion.service';
import { ApiError } from '../../../core/models/api-error';
import { LoginService } from '../services/login';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private readonly formBuilder = inject(FormBuilder);
  private readonly loginService = inject(LoginService);
  private readonly sesionService = inject(SesionService);
  private readonly router = inject(Router);

  readonly enviando = signal(false);
  readonly error = signal<string | null>(null);

  readonly formulario = this.formBuilder.nonNullable.group({
    username: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required],
  });

  iniciarSesion(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    this.enviando.set(true);
    this.error.set(null);

    this.loginService.iniciarSesion(this.formulario.getRawValue()).subscribe({
      next: (respuesta) => {
        this.sesionService.guardarToken(respuesta.token);
        this.enviando.set(false);
        this.router.navigate(['/habitaciones']);
      },
      error: (error: ApiError) => {
        this.error.set(error.mensaje);
        this.enviando.set(false);
      },
    });
  }
}
