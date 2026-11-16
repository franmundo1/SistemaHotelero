export interface Huesped {
  idExterno: string;
  nombre:    string;
  apellido:  string;
  dni:       string;
  telefono:  string | null;
  email:     string;
}

// Cuerpo que espera el back para crear/editar (HuespedRequestDTO)
export type HuespedRequest = Omit<Huesped, 'idExterno'>;

// Respuesta del DELETE: FISICA = se borro, LOGICA = quedo dado de baja con su historial
export interface BajaHuesped {
  tipo: 'FISICA' | 'LOGICA';
  mensaje: string;
}
