package service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import model.Workout;

public class BackupReader {
	
	private final Path directorio;
	
	/*
	 * 		CONSTRUCTOR QUE INCLUYE LOS ELEMENTOS DE USUARIO
	 * */

	public BackupReader (Path directorio) {
		this.directorio= directorio;
	}
		
		public Map<String, Workout> leerWorkouts() throws IOException{
			
			Path origen = directorio.resolve("workouts.dat");
			
			try (ObjectInputStream entrada = 
										new ObjectInputStream(
											new BufferedInputStream(
												Files.newInputStream(origen)))){
				
				Object objeto = entrada.readObject();
				
				//	VERIFICAR QUE LO QUE SE ESTA LEYENDO SEA UN MAP
				if(!(objeto instanceof Map<?, ?>)) {
					throw new IOException("El archivo no contine un Map valido");
				}
				
				Map<?, ?> mapaLeido = (Map<?, ?>) objeto;
				
				Map<String, Workout> workouts = new HashMap<>();
				
				//	 VERIFICAR CADA ENTRADA DEL MAPA
				for(Map.Entry<?, ?> entry: mapaLeido.entrySet()) {
					
					//	VALIDAR CLAVE
					if(!(entry.getKey() instanceof String)) {
						throw new IOException("El backup contine un id invalido");
					}
					
					String id = (String) entry.getKey();
					
					if (id.isBlank()) {
						throw new IOException("El backup contine un id vacio");
					}
					
					Workout workout = (Workout)entry.getValue();
					
					//	COMPROBAR QUE LOS ID COINCIDEN
					if(workout.getId()==null || !id.equals(workout.getId())) {
						throw new IOException("El identificador del workout no coincide: "+id);
					}
					workouts.put(id, workout);
				}
				return workouts;
				
			} catch(ClassNotFoundException exc) {
				throw new IOException("No se pudo reconstruir una clase del backup", exc);
			}
		}
	}
