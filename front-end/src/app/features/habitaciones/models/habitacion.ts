export type TipoHabitacion = 'SIMPLE' | 'DOBLE' | 'SUITE';

export type EstadoOcupacion =
  | 'DISPONIBLE'
  | 'OCUPADA'
  | 'MANTENIMIENTO';

export type EstadoLimpieza =
  | 'LIMPIA'
  | 'SUCIA'
  | 'EN_LIMPIEZA';

export interface Habitacion {
  idExterno: string;
  numero: string;
  tipo: TipoHabitacion;
  capacidad: number;
  precioPorNoche: number;
  estadoOcupacion: EstadoOcupacion;
  estadoLimpieza: EstadoLimpieza;
  activa: boolean;
}
