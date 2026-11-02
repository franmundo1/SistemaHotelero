import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Api } from '../../../core/services/api';
import { Producto } from '../models/consumo';

@Injectable({
  providedIn: 'root',
})
export class ProductosService {
  private readonly api = inject(Api);

  listar(): Observable<Producto[]> {
    return this.api.get<Producto[]>('/api/productos');
  }
}
