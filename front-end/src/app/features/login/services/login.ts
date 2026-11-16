import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Api } from '../../../core/services/api';
import { LoginRequest, LoginResponse } from '../models/login';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  private readonly api = inject(Api);

  iniciarSesion(credenciales: LoginRequest): Observable<LoginResponse> {
    return this.api.post<LoginResponse>('/auth/login', credenciales);
  }
}
