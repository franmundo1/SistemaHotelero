// Roles que vienen en el token (claim "roles")
export const ROLES = {
  ADMIN: 'ROLE_ADMIN',
  RECEPCIONISTA: 'ROLE_RECEPCIONISTA',
  LIMPIEZA: 'ROLE_LIMPIEZA',
} as const;

export interface OpcionMenu {
  texto: string;
  ruta: string;
  roles: string[];
}

// Opciones del menu y que roles las ven (el admin ve todas clarament)
export const OPCIONES_MENU: OpcionMenu[] = [
  { texto: 'Habitaciones', ruta: '/habitaciones', roles: [ROLES.ADMIN, ROLES.RECEPCIONISTA] },
  { texto: 'Disponibilidad', ruta: '/disponibilidad', roles: [ROLES.ADMIN, ROLES.RECEPCIONISTA] },
  { texto: 'Reservas', ruta: '/reservas', roles: [ROLES.ADMIN, ROLES.RECEPCIONISTA] },
  { texto: 'Huéspedes', ruta: '/huespedes', roles: [ROLES.ADMIN, ROLES.RECEPCIONISTA] },
  { texto: 'Consumos', ruta: '/consumos', roles: [ROLES.ADMIN, ROLES.RECEPCIONISTA] },
  { texto: 'Check-in', ruta: '/check-in', roles: [ROLES.ADMIN, ROLES.RECEPCIONISTA] },
  { texto: 'Check-out', ruta: '/check-out', roles: [ROLES.ADMIN, ROLES.RECEPCIONISTA] },
  { texto: 'Limpieza', ruta: '/limpieza', roles: [ROLES.ADMIN, ROLES.LIMPIEZA] },
];
