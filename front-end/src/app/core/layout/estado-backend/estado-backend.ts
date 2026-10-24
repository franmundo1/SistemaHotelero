import { Component, inject, OnInit, signal } from '@angular/core';
import { Api } from '../../services/api';
import { ApiError } from '../../models/api-error';
import { environment } from '../../../../environments/environment';

type Estado = 'cargando' | 'ok' | 'error';

// Muestra si el front logra hablar con el backend (prueba de H3)
@Component({
  selector: 'app-estado-backend',
  templateUrl: './estado-backend.html',
  styleUrl: './estado-backend.css',
})
export class EstadoBackend implements OnInit {
  private readonly apiService = inject(Api);

  protected readonly apiUrl = environment.apiUrl;
  readonly estado = signal<Estado>('cargando');
  readonly error = signal<string | null>(null);

  ngOnInit(): void {
    this.onVerificar();
  }

  onVerificar(): void {
    this.estado.set('cargando');
    this.error.set(null);

    this.apiService.verificarConexion().subscribe({
      next: () => this.estado.set('ok'),
      error: (e: ApiError) => {
        this.error.set(e.mensaje);
        this.estado.set('error');
      },
    });
  }
}
