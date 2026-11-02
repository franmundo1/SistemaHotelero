import { Routes } from '@angular/router';
import { ROLES } from './core/auth/roles';
import { authGuard, guestGuard } from './core/guards/auth-guard';
import { rolGuard } from './core/guards/rol-guard';
import { ListadoHabitaciones } from './features/habitaciones/pages/listado-habitaciones';
import { Login } from './features/login/pages/login';

// Pantallas de recepcion: las ven el admin y el recepcionista
const RECEPCION = [ROLES.ADMIN, ROLES.RECEPCIONISTA];

export const routes: Routes = [
  {
    path: 'login',
    component: Login,
    canActivate: [guestGuard],
  },
  {
    path: 'habitaciones',
    component: ListadoHabitaciones,
    canActivate: [authGuard, rolGuard],
    data: { roles: RECEPCION },
  },
  {
    path: 'disponibilidad',
    loadComponent: () =>
      import('./features/habitaciones/pages/disponibilidad-habitaciones').then(
        (m) => m.DisponibilidadHabitaciones
      ),
    title: 'Disponibilidad',
    canActivate: [authGuard, rolGuard],
    data: { roles: RECEPCION },
  },
  {
    path: 'reservas/nueva',
    loadComponent: () => import('./features/reservas/pages/nueva-reserva').then((m) => m.NuevaReserva),
    title: 'Nueva reserva',
    canActivate: [authGuard, rolGuard],
    data: { roles: RECEPCION },
  },
  {
    path: 'reservas',
    loadComponent: () =>
      import('./features/reservas/pages/listado-reservas').then((m) => m.ListadoReservas),
    title: 'Reservas',
    canActivate: [authGuard, rolGuard],
    data: { roles: RECEPCION },
  },
  {
    path: 'huespedes',
    loadComponent: () =>
      import('./features/huespedes/pages/listado-huespedes').then((m) => m.ListadoHuespedes),
    title: 'Huéspedes',
    canActivate: [authGuard, rolGuard],
    data: { roles: RECEPCION },
  },
  {
    path: 'consumos',
    loadComponent: () =>
      import('./features/consumos/pages/registrar-consumo').then((m) => m.RegistrarConsumo),
    title: 'Registrar consumo',
    canActivate: [authGuard, rolGuard],
    data: { roles: RECEPCION },
  },
  {
    path: 'check-in',
    loadComponent: () => import('./features/check-in/pages/check-in').then((m) => m.RegistrarCheckIn),
    title: 'Registrar check-in',
    canActivate: [authGuard, rolGuard],
    data: { roles: RECEPCION },
  },
  {
    path: 'sin-permiso',
    loadComponent: () => import('./core/layout/sin-permiso/sin-permiso').then((m) => m.SinPermiso),
    title: 'Sin permisos',
    canActivate: [authGuard],
  },
  {
    path: 'check-out',
    loadComponent: () =>
      import('./features/check-out/pages/registrar-check-out').then(
        (m) => m.RegistrarCheckOut
      ),
    title: 'Registrar check-out',
    canActivate: [authGuard],
  },
  {
    path: '',
    redirectTo: 'habitaciones',
    pathMatch: 'full',
  },
  {
    path: '**',
    redirectTo: 'habitaciones',
  },
];
