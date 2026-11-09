// Respuesta de GET /api/dashboard/resumen (DashboardResumenDTO)
export interface DashboardResumen {
  habitacionesTotales:       number;
  habitacionesDisponibles:   number;
  habitacionesOcupadas:      number;
  habitacionesMantenimiento: number;
  habitacionesSucias:        number;
  habitacionesEnLimpieza:    number;
  reservasTotales:           number;
  reservasConfirmadas:       number;
  reservasEnCurso:           number;
  reservasFinalizadas:       number;
  reservasCanceladas:        number;
  productosActivos:          number;
  consumosRegistrados:       number;
}

// Graficos dinamicos (GET /api/dashboard/grafico)
export type TipoGrafico = 'TORTA' | 'DONA' | 'BARRAS';
export type TipoMetrica = 'INGRESOS' | 'RESERVAS' | 'NOCHES_VENDIDAS' | 'CONSUMOS';
export type TipoAgrupacion =
  | 'HABITACION'
  | 'TIPO_HABITACION'
  | 'CANAL'
  | 'ESTADO_RESERVA'
  | 'METODO_PAGO'
  | 'PRODUCTO';

export interface GraficoRequest {
  tipoGrafico: TipoGrafico;
  metrica: TipoMetrica;
  agruparPor: TipoAgrupacion;
  fechaDesde: string;
  fechaHasta: string;
  limite: number;
}

export interface GraficoDinamico {
  tipoGrafico: TipoGrafico;
  titulo: string;
  etiquetas: string[];
  valores: number[];
  total: number;
}

export interface Opcion<T> {
  valor: T;
  texto: string;
}

export const TIPOS_GRAFICO: Opcion<TipoGrafico>[] = [
  { valor: 'TORTA', texto: 'Torta' },
  { valor: 'DONA', texto: 'Dona' },
  { valor: 'BARRAS', texto: 'Barras' },
];

export const METRICAS: Opcion<TipoMetrica>[] = [
  { valor: 'INGRESOS', texto: 'Ingresos' },
  { valor: 'RESERVAS', texto: 'Reservas' },
  { valor: 'NOCHES_VENDIDAS', texto: 'Noches vendidas' },
  { valor: 'CONSUMOS', texto: 'Consumos' },
];

export const AGRUPACIONES: Opcion<TipoAgrupacion>[] = [
  { valor: 'HABITACION', texto: 'Habitación' },
  { valor: 'TIPO_HABITACION', texto: 'Tipo de habitación' },
  { valor: 'CANAL', texto: 'Canal' },
  { valor: 'ESTADO_RESERVA', texto: 'Estado de la reserva' },
  { valor: 'METODO_PAGO', texto: 'Método de pago' },
  { valor: 'PRODUCTO', texto: 'Producto' },
];

// Mismas combinaciones que acepta el back para cada metrica
export const AGRUPACIONES_POR_METRICA: Record<TipoMetrica, TipoAgrupacion[]> = {
  INGRESOS: ['HABITACION', 'TIPO_HABITACION', 'CANAL', 'METODO_PAGO'],
  RESERVAS: ['HABITACION', 'TIPO_HABITACION', 'CANAL', 'ESTADO_RESERVA'],
  NOCHES_VENDIDAS: ['HABITACION', 'TIPO_HABITACION', 'CANAL'],
  CONSUMOS: ['PRODUCTO', 'HABITACION'],
};
