import { computed, Injectable, signal } from '@angular/core';

interface JwtPayload {
  sub: string;
  roles: string[];
  iat: number;
  exp: number;
}

@Injectable({
  providedIn: 'root',
})
export class SesionService {
  private readonly nombreToken = 'sistema_hotelero_token';
  private readonly tokenInterno = signal<string | null>(null);

  readonly token = this.tokenInterno.asReadonly();

  readonly datosUsuario = computed(() =>
    this.decodificarToken(this.tokenInterno())
  );

  readonly autenticado = computed(() => {
    const datos = this.datosUsuario();

    return datos !== null && datos.exp * 1000 > Date.now();
  });

  readonly emailUsuario = computed(() => this.datosUsuario()?.sub ?? null);

  readonly autoridades = computed(() => this.datosUsuario()?.roles ?? []);

  constructor() {
    this.recuperarTokenGuardado();
  }

  guardarToken(token: string): void {
    localStorage.setItem(this.nombreToken, token);
    this.tokenInterno.set(token);
  }

  obtenerToken(): string | null {
    return this.autenticado() ? this.tokenInterno() : null;
  }

  cerrarSesion(): void {
    localStorage.removeItem(this.nombreToken);
    this.tokenInterno.set(null);
  }

  tieneAutoridad(autoridad: string): boolean {
    return this.autoridades().includes(autoridad);
  }

  private recuperarTokenGuardado(): void {
    const token = localStorage.getItem(this.nombreToken);

    if (token === null) {
      return;
    }

    const datos = this.decodificarToken(token);

    if (datos === null || datos.exp * 1000 <= Date.now()) {
      localStorage.removeItem(this.nombreToken);
      return;
    }

    this.tokenInterno.set(token);
  }

  private decodificarToken(token: string | null): JwtPayload | null {
    if (token === null) {
      return null;
    }

    try {
      const contenido = token.split('.')[1];
      const base64 = contenido.replaceAll('-', '+').replaceAll('_', '/');
      const base64Completo = base64.padEnd(
        base64.length + ((4 - (base64.length % 4)) % 4),
        '=',
      );

      return JSON.parse(atob(base64Completo)) as JwtPayload;
    } catch {
      return null;
    }
  }
}
