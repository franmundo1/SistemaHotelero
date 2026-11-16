export interface CheckInRequest {
  idReservaExterno: string;
  observaciones?: string;
}

export interface CheckIn {
  idExterno: string;
  idReservaExterno: string;
  fechaHoraEntrada: string;
  observaciones: string | null;
  nombreHuesped: string;
  numeroHabitacion: string;
  estadoReserva: string;
  estadoOcupacionHabitacion: string;
}
