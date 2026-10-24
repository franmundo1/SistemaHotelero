import { Routes } from '@angular/router';
import { ListadoHabitaciones } from './features/habitaciones/pages/listado-habitaciones';

export const routes: Routes = [
  {
    path: 'habitaciones',
    component: ListadoHabitaciones,
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
