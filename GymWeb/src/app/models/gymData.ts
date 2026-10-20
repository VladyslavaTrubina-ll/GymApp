export interface Serie {
  nombre: string;
  foto: string;
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
  ejercicios: Ejercicio[];
}