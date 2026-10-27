import { Component,OnInit, inject, signal } from '@angular/core';
import { Rutinas } from '../../services/rutinas';
import { RouterLink } from '@angular/router';
import { computed } from '@angular/core';
import { FormsModule } from '@angular/forms';


@Component({
  imports: [RouterLink, FormsModule],
  selector: 'app-vista-general',
  styleUrl: './vista-general.css',
  templateUrl: './vista-general.html',
})
export class VistaGeneral implements OnInit{
rutinasService = inject(Rutinas);

workouts = this.rutinasService.workouts;
cargando = this.rutinasService.cargando;
error = this.rutinasService.error;

ngOnInit(): void {
  this.rutinasService.getGymData();
}

//FILTRO y BUSQUEDA UNIDOS

nivelSeleccionado = this.rutinasService.nivelSeleccionado;

workoutsFiltroYBusqueda = computed(() => {
  
const lista = this.rutinasService.workouts();
const filtro = this.rutinasService.nivelSeleccionado();
const busqueda = this.rutinasService.busquedaInput().toLowerCase().trim();


  return lista.filter(workout => {
    
    const nivel = Number(workout.nivel);
    let filtroBien = true;

    if (filtro === 'Principiante') {
      filtroBien = nivel >= 0 && nivel <= 1;
    } else if (filtro === 'Intermedio') {
      filtroBien = nivel === 2;
    } else if (filtro === 'Avanzado') {
      filtroBien = nivel === 3;
    }

  const nombreWorkout = workout.nombre.toLowerCase();
  const buscadorBien = !busqueda || nombreWorkout.includes(busqueda)

    return filtroBien && buscadorBien;
});
});

}