import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'formatoDuracion',
})
export class FormatoDuracionPipe implements PipeTransform {
  transform(totalSegundos: number): string {
    if (!totalSegundos || totalSegundos <= 0) {
      return '00:00:00';
    }
    const horas = Math.floor(totalSegundos / 3600);
    const minutos = Math.floor((totalSegundos % 3600) / 60);
    const segundos = totalSegundos % 60;

    const horaString = horas.toString().padStart(2, '0');
    const minutoString = minutos.toString().padStart(2, '0');
    const segundoString = segundos.toString().padStart(2, '0');

    let resultado = `${horaString}:${minutoString}:${segundoString}`;

    return resultado; //formateo
  }
}


