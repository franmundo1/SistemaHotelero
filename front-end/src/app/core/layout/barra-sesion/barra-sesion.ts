import { Component, HostListener, computed, inject, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { NavigationEnd, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { filter, map } from 'rxjs';
import { GRUPOS_MENU, GrupoMenu, ROLES } from '../../auth/roles';
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

  // Estado local de la barra con signals
  readonly menuAbierto = signal(false);
  readonly grupoAbierto = signal<string | null>(null);
  readonly menuUsuarioAbierto = signal(false);

  // Ruta actual, para marcar el grupo de la pantalla en la que esta el usuario
  private readonly rutaActual = toSignal(
    this.router.events.pipe(
      filter((evento) => evento instanceof NavigationEnd),
      map(() => this.router.url)
    ),
    { initialValue: this.router.url }
  );

  // Solo los grupos y opciones que puede ver el rol del usuario
  readonly grupos = computed<GrupoMenu[]>(() =>
    GRUPOS_MENU.map((grupo) => ({
      texto: grupo.texto,
      opciones: grupo.opciones.filter((opcion) => this.sesionService.tieneAlgunRol(opcion.roles)),
    })).filter((grupo) => grupo.opciones.length > 0)
  );

  readonly puedeReservar = computed(() =>
    this.sesionService.tieneAlgunRol([ROLES.ADMIN, ROLES.RECEPCIONISTA])
  );

  readonly rutaInicio = this.sesionService.rutaInicio;

  readonly inicialUsuario = computed(() => this.nombreUsuario()?.charAt(0).toUpperCase() ?? 'U');

  readonly fechaActual = new Intl.DateTimeFormat('es-AR', {
    weekday: 'long',
    day: 'numeric',
    month: 'long',
  }).format(new Date());

  grupoActivo(grupo: GrupoMenu): boolean {
    const ruta = this.rutaActual();
    return grupo.opciones.some((opcion) => ruta.startsWith(opcion.ruta));
  }

  cambiarMenu(): void {
    this.menuAbierto.update((abierto) => !abierto);
  }

  cambiarGrupo(texto: string): void {
    this.menuUsuarioAbierto.set(false);
    this.grupoAbierto.update((abierto) => (abierto === texto ? null : texto));
  }

  cambiarMenuUsuario(): void {
    this.grupoAbierto.set(null);
    this.menuUsuarioAbierto.update((abierto) => !abierto);
  }

  cerrarMenu(): void {
    this.menuAbierto.set(false);
    this.grupoAbierto.set(null);
    this.menuUsuarioAbierto.set(false);
  }

  // Un clic fuera de la barra cierra los desplegables
  @HostListener('document:click', ['$event'])
  alHacerClic(evento: MouseEvent): void {
    const objetivo = evento.target as HTMLElement;
    if (!objetivo.closest('app-barra-sesion')) {
      this.grupoAbierto.set(null);
      this.menuUsuarioAbierto.set(false);
    }
  }

  // Escape cierra cualquier menu abierto
  @HostListener('document:keydown.escape')
  alPresionarEscape(): void {
    this.cerrarMenu();
  }

  cerrarSesion(): void {
    this.cerrarMenu();
    this.sesionService.cerrarSesion();
    this.router.navigate(['/login'], {
      replaceUrl: true,
    });
  }
}
