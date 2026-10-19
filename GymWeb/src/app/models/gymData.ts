export interface Serie {
  nombre: string;
  repeticiones: number;
  duracion: number;
  descanso: number;
  foto: string;
}

export interface Ejercicio {
  id: number;
  nombre: string;
  descripcion: string;
  foto: string;
  series: Serie[];
}

export interface Workout {
  id: number;
  nombre: string;
  nivel: number;
  ejercicios: Ejercicio[];
  tiempoTotal?: number;
  tiempoPrevisto?: number;
  fecha?: string;
  porcentajeCompletado?: number;
}