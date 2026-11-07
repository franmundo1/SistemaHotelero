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
