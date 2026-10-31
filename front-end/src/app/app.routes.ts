import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth-guard';
import { ListadoHabitaciones } from './features/habitaciones/pages/listado-habitaciones';
import { Login } from './features/login/pages/login';

export const routes: Routes = [
  {
    path: 'login',
    component: Login,
  },
  {
    path: 'habitaciones',
    component: ListadoHabitaciones,
    canActivate: [authGuard],
  },
  {
    path: 'disponibilidad',
    loadComponent: () =>
      import('./features/habitaciones/pages/disponibilidad-habitaciones').then(
        (m) => m.DisponibilidadHabitaciones
      ),
    title: 'Disponibilidad',
    canActivate: [authGuard],
  },
  {
    path: 'reservas/nueva',
    loadComponent: () => import('./features/reservas/pages/nueva-reserva').then((m) => m.NuevaReserva),
    title: 'Nueva reserva',
    canActivate: [authGuard],
  },
  {
    path: 'huespedes',
    loadComponent: () =>
      import('./features/huespedes/pages/listado-huespedes').then((m) => m.ListadoHuespedes),
    title: 'Huéspedes',
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
