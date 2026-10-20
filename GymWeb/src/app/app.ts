import { Component, OnInit, inject , signal} from '@angular/core';
import { Rutinas } from './services/rutinas';
import { RouterOutlet } from '@angular/router';

@Component({
  imports: [RouterOutlet],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})

//esto es una prueba debido a que la base de datos aún no está terminada

export class App  {
  
  title = "GymWeb";
  
}
