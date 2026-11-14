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

export interface GrupoMenu {
  texto: string;
  opciones: OpcionMenu[];
}

const RECEPCION = [ROLES.ADMIN, ROLES.RECEPCIONISTA];

// Opciones del menu agrupadas segun el flujo del hotel y que roles las ven (el admin ve todas)
export const GRUPOS_MENU: GrupoMenu[] = [
  {
    texto: 'Reservas',
    opciones: [
      { texto: 'Reservas', ruta: '/reservas', roles: RECEPCION },
      { texto: 'Disponibilidad', ruta: '/disponibilidad', roles: RECEPCION },
      { texto: 'Huéspedes', ruta: '/huespedes', roles: RECEPCION },
    ],
  },
  {
    texto: 'Estadías',
    opciones: [
      { texto: 'Check-in', ruta: '/check-in', roles: RECEPCION },
      { texto: 'Check-out', ruta: '/check-out', roles: RECEPCION },
      { texto: 'Consumos', ruta: '/consumos', roles: RECEPCION },
    ],
  },
  {
    texto: 'Habitaciones',
    opciones: [
      { texto: 'Habitaciones', ruta: '/habitaciones', roles: RECEPCION },
      { texto: 'Limpieza', ruta: '/limpieza', roles: [ROLES.ADMIN, ROLES.LIMPIEZA] },
    ],
  },
];

// Lista plana con todas las opciones, por si se necesita recorrerlas sin grupos
export const OPCIONES_MENU: OpcionMenu[] = GRUPOS_MENU.flatMap((grupo) => grupo.opciones);
