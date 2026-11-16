export interface CheckOutResumen {
  idReservaExterno: string;
  huesped: string;
  habitacion: string;
  totalEstadia: number;
  totalConsumos: number;
  anticipo: number;
  totalFinal: number;
  saldoPendiente: number;
}

export interface CheckOutRequest {
  idReservaExterno: string;
  montoAbonado: number;
  metodoPago: string;
  observaciones?: string;
}

export interface CheckOutResponse {
  idExterno: string;
  idReservaExterno: string;
  fechaHoraSalida: string;
  observaciones: string | null;
  nombreHuesped: string;
  numeroHabitacion: string;
  estadoReserva: string;
  estadoOcupacionHabitacion: string;
  estadoLimpiezaHabitacion: string;
  totalEstadia: number;
  anticipo: number;
  montoFinal: number;
  totalConsumos: number;
  totalFinal: number;
}
