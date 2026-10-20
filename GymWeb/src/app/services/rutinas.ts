import { RouterOutlet } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Workout } from '../models/gymData';

@Injectable({providedIn: 'root'} )
export class Rutinas {

    private httpclient = inject(HttpClient);
    private API_JSON : string = "assets/data/prueba.json";

  workouts = signal<Workout[]>([]);
  cargando = signal<boolean>(false);
  error = signal<string | null>(null);


  constructor() {
    this.getGymData();
  }
  
   getGymData() {
    this.httpclient.get<{ workouts: Workout[] }>(this.API_JSON).subscribe({
      next: (data) => {
        this.workouts.set(data.workouts);
      },
      error: (err) => console.error("Ha ocurrido un error: " , err)
      
    });
  }
}

