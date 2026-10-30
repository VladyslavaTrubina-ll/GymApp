package service;

import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import model.Workout;

public class BackupReader {
	
	private final Path directorio;
	
	/*
	 * 		CONSTRUCTOR QUE INCLUYE LOS ELEMENTOS DE USUARIO
	 * */

	public BackupReader (Path directorio) {
		this.directorio= directorio;
		
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
				
				
			}
		}
	}
}
