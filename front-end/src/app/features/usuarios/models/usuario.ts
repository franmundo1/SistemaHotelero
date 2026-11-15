// Respuesta del back (UsuarioResponseDTO)
export interface Usuario {
  idExterno: string;
  nombre: string;
  apellido: string;
  email: string;
  rol: string;
  activo: boolean;
}

// Roles que el admin puede asignar: el back no deja crear otro administrador
export type RolEmpleado = 'ROLE_RECEPCIONISTA' | 'ROLE_LIMPIEZA';

export const ROLES_EMPLEADO: { valor: RolEmpleado; texto: string }[] = [
  { valor: 'ROLE_RECEPCIONISTA', texto: 'Recepcionista' },
  { valor: 'ROLE_LIMPIEZA', texto: 'Limpieza' },
];

// Cuerpo que espera el back para crear (UsuarioRequestDTO)
export interface UsuarioRequest {
  nombre: string;
  apellido: string;
  email: string;
  password: string;
  rol: RolEmpleado;
}
