import { Component, inject, OnInit, signal } from '@angular/core';
import { SesionService } from '../../../core/auth/sesion.service';
import { ApiError } from '../../../core/models/api-error';
import { FormularioUsuario } from '../components/formulario-usuario/formulario-usuario';
import { Usuario, UsuarioRequest } from '../models/usuario';
import { UsuariosService } from '../services/usuarios';

@Component({
  selector: 'app-listado-usuarios',
  imports: [FormularioUsuario],
  templateUrl: './listado-usuarios.html',
  styleUrl: './listado-usuarios.css',
})
export class ListadoUsuarios implements OnInit {
  private readonly usuariosService = inject(UsuariosService);
  private readonly sesionService = inject(SesionService);

  readonly usuarios = signal<Usuario[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  readonly formularioAbierto = signal(false);
  readonly guardando = signal(false);
  readonly errorFormulario = signal<string | null>(null);
  readonly mensaje = signal<string | null>(null);
  readonly errorEstado = signal<string | null>(null);
  readonly actualizando = signal<string | null>(null); // id del usuario que se esta activando o desactivando

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.cargando.set(true);
    this.error.set(null);

    this.usuariosService.listar().subscribe({
      next: (usuarios) => {
        this.usuarios.set(usuarios);
        this.cargando.set(false);
      },
      error: (error: ApiError) => {
        this.error.set(error.mensaje);
        this.cargando.set(false);
      },
    });
  }

  onNuevo(): void {
    this.mensaje.set(null);
    this.errorEstado.set(null);
    this.errorFormulario.set(null);
    this.formularioAbierto.set(true);
  }

  onCancelar(): void {
    this.formularioAbierto.set(false);
  }

  onGuardar(datos: UsuarioRequest): void {
    this.guardando.set(true);
    this.errorFormulario.set(null);

    this.usuariosService.create(datos).subscribe({
      next: (usuario) => {
        this.guardando.set(false);
        this.onCancelar();
        this.usuarios.update((lista) => [...lista, usuario]);
        this.mensaje.set(`Usuario ${usuario.email} creado correctamente.`);
      },
      error: (error: ApiError) => {
        this.guardando.set(false);
        this.errorFormulario.set(error.mensaje);
      },
    });
  }

  onCambiarActivo(usuario: Usuario): void {
    const activar = !usuario.activo;
    if (!activar) {
      const confirmado = confirm(
        `¿Seguro que querés desactivar a ${usuario.nombre} ${usuario.apellido}? No va a poder iniciar sesión.`
      );
      if (!confirmado) {
        return;
      }
    }

    this.actualizando.set(usuario.idExterno);
    this.mensaje.set(null);
    this.errorEstado.set(null);

    this.usuariosService.cambiarActivo(usuario.idExterno, activar).subscribe({
      next: (actualizado) => {
        // Se reemplaza solo la fila que cambio
        this.usuarios.update((lista) =>
          lista.map((u) => (u.idExterno === actualizado.idExterno ? actualizado : u))
        );
        this.mensaje.set(
          `Usuario ${actualizado.email} ${actualizado.activo ? 'activado' : 'desactivado'}.`
        );
        this.actualizando.set(null);
      },
      error: (error: ApiError) => {
        this.errorEstado.set(error.mensaje);
        this.actualizando.set(null);
      },
    });
  }

  // El admin no puede desactivarse a si mismo: se quedaria sin acceso
  esUsuarioActual(usuario: Usuario): boolean {
    return usuario.email === this.sesionService.emailUsuario();
  }

  mostrarRol(rol: string): string {
    const texto = rol.replace('ROLE_', '').toLowerCase();
    return texto.charAt(0).toUpperCase() + texto.slice(1);
  }
}
