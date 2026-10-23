import { Component,OnInit, inject, signal } from '@angular/core';
import { Rutinas } from '../../services/rutinas';
import { RouterLink } from '@angular/router';
import { computed } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { form } from '@angular/forms/signals';

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

nivelSeleccionado = signal<string>("");

workoutsFiltro = computed(() => {
  
  const lista = this.rutinasService.workouts();
const filtro = this.nivelSeleccionado();

if (!filtro) {

  return lista;

} else {

  return lista.filter(workout => {
    const nivel = Number(workout.nivel);

    if (filtro === 'Principiante') {
      return nivel >= 0 && nivel <= 1;
    } else if (filtro === 'Intermedio') {
      return nivel === 2;
    } else if (filtro === 'Avanzado') {
      return nivel === 3;
    }

    return true;
});
    }
  });
}