import { Component, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Rutinas } from '../../../services/rutinas';

@Component({
  imports: [],
  selector: 'app-vista-detalle',
  styleUrl: './vista-detalle.css',
  templateUrl: './vista-detalle.html',
})
export class VistaDetalle implements OnInit {
  private route = inject(ActivatedRoute);
  private rutinasService = inject(Rutinas);

  rutinaSeleccionada = signal<any>(null);

  ngOnInit(): void {

    this.route.paramMap.subscribe(params => {
      const parametroNombre = params.get('nombre');

      if (parametroNombre) {
        // Buscamos dentro del array de workouts
        const encontrada = this.rutinasService.workouts().find(
          w => w.nombre.toLowerCase() === parametroNombre.toLowerCase()
        );

        this.rutinaSeleccionada.set(encontrada);
      }
    });
  }

  funcionDuracionEjercicio (descanso: number, duracionSerie: number, numSeries: number) {
    return (duracionSerie * numSeries) + descanso;
  }

}