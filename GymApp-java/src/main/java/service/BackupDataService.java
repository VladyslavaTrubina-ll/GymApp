package service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.DocumentSnapshot;

import dao.WorkoutDAO;
import model.User;
import model.Workout;


public class BackupDataService{
	
	private final WorkoutDAO workoutDAO;
	private final UserService userService;

	
	public BackupDataService(WorkoutDAO workoutDAO, UserService userService) {
		this.workoutDAO= workoutDAO;
		this.userService = userService;
		
	}
	
	// BACKUP WORKOUTS
	
	/**
	 * Recupera todo lo relativo a workouts y conforma el HashMap para escribir la copia de respaldo
	 * 
	 * @return HashMap de workouts con su id
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public Map<String, Workout> obtenerWorkoutsBackup() throws IOException,
												InterruptedException, ExecutionException{
		
		Map<String, Workout> workoutsBackup= new HashMap<>();
		List<Workout> workouts = workoutDAO.obtenerTodosCompleto();	
		
		// verifico que la lista recuperada con los DAO no sea nula
		if (workouts == null) {
			throw new IllegalStateException("No se pudieron cargar los workouts");
		}
		
		for (Workout workout : workouts) {
			// VERIFICA LOS IDENTIFICADORES
		    if (workout == null ||
		        workout.getId() == null ||
		        workout.getId().isBlank()) {

		        throw new IllegalStateException(
		            "Se encontró un workout sin identificador válido"
		        );
		    }

		    String id = workout.getId();
		    // PREVIENE IDENTIFICADORES DUPLICADOS
		    if (workoutsBackup.putIfAbsent(id, workout) != null) {

		        throw new IllegalStateException(
		            "Identificador de workout duplicado: " + id
		        );
		    }
		}
		return workoutsBackup;
		
				
	}
	//-----------------------------------------
	//	BACKUP USUARIOS
	public Map<String, User> obtenerUsuariosBackup(String uid) throws Exception{
		
		Map<String, User> usuariosBackup = new HashMap<>();
		
		//	VERIFICAR QUE EL UID RECIBIDO SEA VALIDO
		if (uid == null || uid.isBlank()) {
			throw new IllegalArgumentException("El UID del usuario no es valido");
		}
		
		//	RECUPERAR DOCUMENTOS DESDE FIRESTORE CON LOS METODOS DE USERSERVICE
		DocumentSnapshot documento = userService.getUserProfileAsync(uid);
		
		//	VERIFICAMOS QUE EL DOCUMENTO EXISTE
		if(documento == null || !documento.exists()) {
			throw new IllegalStateException("No se pudo recuperar el perfil de usuario");
		}
		//	COVERTIMOS A USER
		User usuarioFirestore = documento.toObject(User.class);
		
		//	VERIFICAMOS QUE LA CONVERSION FUE CORRECTA, QUE EL NUEVO OBJETO USER NO ES NULO
		if(usuarioFirestore == null) {
			throw new IllegalStateException("No se pudo recuperar el perfil de usuario");
		}
				
		//	VERIFICAmos QUE EL CORREO ES VALIDO
		if(usuarioFirestore.getEmail()==null || usuarioFirestore.getEmail().isBlank()) {
			throw new IllegalStateException("El usuario no tiene un correo valido");
		}
		
		//	CREAMOS UN OBJETO USUARIO PARA AGREGARLO AL HASHMAP
		User usuario = new User(
								uid,
								usuarioFirestore.getFirstName(),
								usuarioFirestore.getLastName(),
								usuarioFirestore.getEmail(),
								usuarioFirestore.getBirthDate(),
								usuarioFirestore.getLevel());
		usuariosBackup.put(uid, usuario);
		
		return usuariosBackup;
	}

}