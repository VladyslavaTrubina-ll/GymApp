export interface Serie {
  nombre: string;
  repeticiones: number;
  duracion: number;
}

export interface Ejercicio {
  nombre: string;
  descripcion: string;
  descanso: number;
  series: Serie[];
}

export interface Workout {
  nombre: string;
  descripcion: string;
  nivel: number;
  video: string;
  imagen: string;
  ejercicios: Ejercicio[];
}