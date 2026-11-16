import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Api } from '../../../core/services/api';
import { Usuario, UsuarioRequest } from '../models/usuario';

@Injectable({
  providedIn: 'root',
})
export class UsuariosService {
  private readonly api = inject(Api);
  private readonly url = '/api/usuarios';

  listar(): Observable<Usuario[]> {
    return this.api.get<Usuario[]>(this.url);
  }

  create(usuario: UsuarioRequest): Observable<Usuario> {
    return this.api.post<Usuario>(this.url, usuario);
  }

  // Activa o desactiva: el estado viaja como parametro (?activo=true), con cuerpo vacio
  cambiarActivo(idExterno: string, activo: boolean): Observable<Usuario> {
    return this.api.patch<Usuario>(`${this.url}/${idExterno}/activo`, {}, { activo });
  }
}
