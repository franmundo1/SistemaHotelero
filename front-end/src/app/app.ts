import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SesionService } from './core/auth/sesion.service';
import { BarraSesion } from './core/layout/barra-sesion/barra-sesion';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, BarraSesion],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  private readonly sesionService = inject(SesionService);

  readonly autenticado = this.sesionService.autenticado;
}
