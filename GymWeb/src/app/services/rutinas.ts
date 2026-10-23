import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Workout } from '../models/gymData';

@Injectable({providedIn: 'root'} )
export class Rutinas {

    private httpclient = inject(HttpClient);
    private jsonURL : string = "assets/data/prueba.json";

  workouts = signal<Workout[]>([]);
  cargando = signal<boolean>(false);
  error = signal<string | boolean>(false);

  nivelSeleccionado = signal<string>("");
  busquedaInput = signal<string>("");

  constructor() {
    this.getGymData();
  }
  
   getGymData() {

    this.cargando.set(false);
    this.error.set(false);

    this.httpclient.get<{ workouts: Workout[] }>(this.jsonURL).subscribe({
      next: (data) => {
        this.workouts.set(data.workouts);
        this.cargando.set(false);
      },

      error: (err) => {

        this.error.set("No se han podido cargar los datos del gimnasio");
        this.cargando.set(false);
      }
      
    });
  }
}


