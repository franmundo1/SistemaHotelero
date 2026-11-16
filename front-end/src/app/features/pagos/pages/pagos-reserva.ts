import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ApiError } from '../../../core/models/api-error';
import { Pago } from '../models/pago';
import { PagosService } from '../services/pagos';

@Component({
  selector: 'app-pagos-reserva',
  imports: [CurrencyPipe, DatePipe, RouterLink],
  templateUrl: './pagos-reserva.html',
  styleUrl: './pagos-reserva.css',
})
export class PagosReserva implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly pagosService = inject(PagosService);

  readonly pagos = signal<Pago[]>([]);
  readonly cargando = signal(false);
  readonly error = signal<string | null>(null);
  idReserva = '';

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.idReserva = params['id'];
      this.cargarPagos();
    });
  }

  cargarPagos(): void {
    if (!this.idReserva) {
      this.error.set('No se encontró la reserva');
      return;
    }

    this.cargando.set(true);
    this.error.set(null);

    this.pagosService.listarPorReserva(this.idReserva).subscribe({
      next: (pagos) => {
        this.pagos.set(pagos);
        this.cargando.set(false);
      },
      error: (error: ApiError) => {
        this.error.set(error.mensaje);
        this.cargando.set(false);
      },
    });
  }

  mostrarTexto(texto: string): string {
    const resultado = texto.replaceAll('_', ' ').toLowerCase();

    return resultado.charAt(0).toUpperCase() + resultado.slice(1);
  }
}
