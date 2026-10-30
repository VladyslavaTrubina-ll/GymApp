package service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import dao.WorkoutDAO;
import model.Workout;


public class BackupDataService{
	
	private final WorkoutDAO workoutDAO;


	/*
     * TODO: INTEGRACIÓN FUTURA DE USUARIOS
     *
     * Se incorporará una dependencia para recuperar
     * la información de usuarios.
     *
     * Tipo previsto:
     *
     * private final UserDAO userDAO;
     *
     * O la clase de acceso a datos que finalmente
     * se acuerde con el equipo.
     */
	
	public BackupDataService(WorkoutDAO workoutDAO) {
		this.workoutDAO= workoutDAO;
		 /*
         * TODO: INTEGRACIÓN FUTURA DE USUARIOS
         *
         * Se incorporará la dependencia correspondiente
         * al constructor cuando se defina su implementación.
         */
	}
	
	public Map<String, Workout> obtenerWorkoutsBackup() throws IOException,
												InterruptedException, ExecutionException{
		
		Map<String, Workout> workoutsBackup= new HashMap<>();
		List<Workout> workouts = workoutDAO.obtenerTodosCompleto();	
		
		// verifico que no haya workouts nulos
		if (workouts == null) {
			throw new IllegalStateException("No se pudieron cargar los workouts");
		}
		
		for (Workout workout : workouts) {

		    if (workout == null ||
		        workout.getId() == null ||
		        workout.getId().isBlank()) {

		        throw new IllegalStateException(
		            "Se encontró un workout sin identificador válido"
		        );
		    }

		    String id = workout.getId();

		    if (workoutsBackup.putIfAbsent(id, workout) != null) {

		        throw new IllegalStateException(
		            "Identificador de workout duplicado: " + id
		        );
		    }
		}
		return workoutsBackup;
		
				
	}

}
