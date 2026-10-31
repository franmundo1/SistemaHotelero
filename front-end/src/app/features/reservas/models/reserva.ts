export type MetodoPago = 'EFECTIVO' | 'TRANSFERENCIA' | 'TARJETA' | 'MERCADO_PAGO';

export const METODOS_PAGO: MetodoPago[] = ['EFECTIVO', 'TRANSFERENCIA', 'TARJETA', 'MERCADO_PAGO'];

export interface CanalReserva {
  idExterno: string;
  tipo: string;
}

// Cuerpo que espera el back para crear (ReservaRequestDTO)
export interface ReservaRequest {
  idHuespedExterno:      string;
  numeroHabitacion:      string;
  emailUsuarioCreador:   string;
  idCanalReservaExterno: string;
  fechaEntrada:          string;
  fechaSalida:           string;
  cantidadPersonas:      number;
  anticipo:              number;
  metodoPagoAnticipo:    MetodoPago;
}

export interface Reserva {
  idExterno:         string;
  huesped:           string;
  habitacion:        string;
  usuarioCreador:    string;
  canalReserva:      string;
  fechaEntrada:      string;
  fechaSalida:       string;
  totalEstadia:      number;
  motivoCancelacion: string | null;
  estado:            string;
}
