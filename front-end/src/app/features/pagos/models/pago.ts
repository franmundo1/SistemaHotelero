export interface Pago {
  idExterno: string;
  idReservaExterno: string;
  idUsuarioExterno: string;
  nombreUsuario: string;
  monto: number;
  metodoPago: string;
  tipoPago: string;
  fechaPago: string;
  observaciones: string | null;
}
