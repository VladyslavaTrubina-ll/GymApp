package service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import model.User;
import model.Workout;

public class BackupManager {

	private final BackupDataService dataService;
	private final BackupWritter backupWritter;
	
	
	public BackupManager(BackupDataService dataService, BackupWritter backupWritter) {
		this.dataService= dataService;
		this.backupWritter= backupWritter;
	}
	
	
	/**
	 * LLama al metodo que recupera los workouts y los pasa al BackupWritter
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void realizarBackupWorkouts() throws IOException, InterruptedException, ExecutionException{
		
		Map<String, Workout> workouts = dataService.obtenerWorkoutsBackup();
		
		backupWritter.escribirWorkouts(workouts);
	}
	
	/**
	 * Llama al metodo que recupera los usuarios y los pasa al BackupWritter
	 * @param uid
	 * @throws Exception
	 */
	public void realizarBackupUsuario(String uid) throws Exception {
		
		Map<String,User> usuarios = dataService.obtenerUsuariosBackup(uid);
		
		backupWritter.escribirUsuarios("usuarios.dat", usuarios);
	}
}
