export interface Producto {
  idExterno: string;
  nombre: string;
  categoria: string;
  precio: number;
  stock: number;
  activo: boolean;
}

export interface ConsumoRequest {
  idReservaExterno: string;
  idProductoExterno: string;
  cantidad: number;
  observaciones?: string;
}

export interface Consumo {
  idExterno: string;
  idReservaExterno: string;
  numeroHabitacion: string;
  idProductoExterno: string;
  producto: string;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
  fechaConsumo: string;
  observaciones: string | null;
}

export interface ConsumoItemRequest {
  idProductoExterno: string;
  cantidad: number;
  observaciones?: string;
}

export interface ConsumoLoteRequest {
  idReservaExterno: string;
  consumos: ConsumoItemRequest[];
}

export interface ItemCarritoConsumo {
  idProductoExterno: string;
  producto: string;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
  observaciones?: string;
}
