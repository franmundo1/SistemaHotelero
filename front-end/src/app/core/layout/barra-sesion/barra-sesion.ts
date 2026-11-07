import { Component, computed, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { OPCIONES_MENU, ROLES } from '../../auth/roles';
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

  readonly nombreUsuario = this.sesionService.nombreUsuario;
  readonly menuAbierto = signal(false);

  // Solo las opciones que puede ver el rol del usuario
  readonly opciones = computed(() =>
    OPCIONES_MENU.filter((opcion) => this.sesionService.tieneAlgunRol(opcion.roles))
  );

  readonly puedeReservar = computed(() =>
    this.sesionService.tieneAlgunRol([ROLES.ADMIN, ROLES.RECEPCIONISTA])
  );

  readonly rutaInicio = this.sesionService.rutaInicio;

  readonly inicialUsuario = computed(() => this.nombreUsuario()?.charAt(0).toUpperCase() ?? 'U');

  readonly fechaActual = new Intl.DateTimeFormat('es-AR', {
    weekday: 'short',
    day: 'numeric',
    month: 'short',
  })
    .format(new Date())
    .replace('.', '');

  cambiarMenu(): void {
    this.menuAbierto.update((abierto) => !abierto);
  }

  cerrarMenu(): void {
    this.menuAbierto.set(false);
  }

  cerrarSesion(): void {
    this.cerrarMenu();
    this.sesionService.cerrarSesion();
    this.router.navigate(['/login'], {
      replaceUrl: true,
    });
  }
}
