import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { EstadoBackend } from './core/layout/estado-backend/estado-backend';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, EstadoBackend],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {}
