import { Component,OnInit, inject, signal } from '@angular/core';
import { Rutinas } from '../../services/rutinas';
import { RouterLink } from '@angular/router';

@Component({
  imports: [RouterLink],
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

}
