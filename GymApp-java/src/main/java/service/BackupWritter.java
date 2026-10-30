package service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.util.Map;
import model.Workout;
//import model.User;


public class BackupWritter {
	
	private final Path directorio;
	
	public BackupWritter(Path directorio) {
		this.directorio = directorio;
		
	}
	
	private void escribir(String nombreArchivo, Map<String, ?> datos) throws IOException{
		Files.createDirectories(directorio);
		
		Path destino = directorio.resolve(nombreArchivo);
		Path temporal = Files.createTempFile(directorio, nombreArchivo, ".tmp");
		
		try {
			try(ObjectOutputStream salida = new ObjectOutputStream(
												new BufferedOutputStream(
														Files.newOutputStream(temporal))))
													{salida.writeObject(datos);}
			
			try {
				Files.move(temporal, destino, 
							StandardCopyOption.ATOMIC_MOVE, 
							StandardCopyOption.REPLACE_EXISTING);
			} catch(AtomicMoveNotSupportedException exc) {
				Files.move(temporal, destino, StandardCopyOption.REPLACE_EXISTING);
			}
		} finally {
			Files.deleteIfExists(temporal);
		}
	}

	/*  PENDIENTE DE PUNTUALIZAR EL ESTADO DEL PROCESO DE BACKUPS DE USUARIOS
	public void escribirUsuarios(String nombreArchivo, Map<String, User> datos) throws IOException{
		escribir("usuarios.dat", usuarios);
	} */
	
	public void escribirWorkouts(Map<String,Workout> datos) throws IOException{
		escribir("workouts.dat", datos);
	}

}
