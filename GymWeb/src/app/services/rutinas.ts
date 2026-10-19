import { RouterOutlet } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Serie , Ejercicio , Workout } from '../models/gymData';

@Injectable({providedIn: 'root'} )
export class Rutinas {

    private httpclient = inject(HttpClient);
    private API_JSON : string = "assets/data/prueba.json";

  workouts = signal<Workout[]>([]);
  cargando = signal<boolean>(false);
  error = signal<string | null>(null);

   getGymData() {
    this.cargando.set(true);
    this.error.set(null);

    this.httpclient.get<{ workouts: Workout[] }>(this.API_JSON).subscribe({
      next: (data) => {
        this.workouts.set(data.workouts);
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set('Error al cargar las rutinas');
        this.cargando.set(false);
        console.error("Ha ocurrido un error: " , err);
      }
    });
  }
}