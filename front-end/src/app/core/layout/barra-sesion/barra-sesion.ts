import { Component, computed, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { SesionService } from '../../auth/sesion.service';

@Component({
  selector: 'app-barra-sesion',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './barra-sesion.html',
  styleUrl: './barra-sesion.css',
})
export class BarraSesion {
  private readonly sesionService = inject(SesionService);
  private readonly router = inject(Router);

  readonly emailUsuario = this.sesionService.emailUsuario;

  readonly inicialUsuario = computed(() => this.emailUsuario()?.charAt(0).toUpperCase() ?? 'U');

  readonly fechaActual = new Intl.DateTimeFormat('es-AR', {
    weekday: 'short',
    day: 'numeric',
    month: 'short',
  })
    .format(new Date())
    .replace('.', '');

  cerrarSesion(): void {
    this.sesionService.cerrarSesion();
    this.router.navigate(['/login'], {
      replaceUrl: true,
    });
  }
}
