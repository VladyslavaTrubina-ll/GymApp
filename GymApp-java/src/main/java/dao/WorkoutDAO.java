package dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import model.Exercise;
import model.Workout;
import service.DBconnect;

public class WorkoutDAO {
	
	private final ExerciseDAO exerciseDao;
	
	
	
	public WorkoutDAO(ExerciseDAO exerciseDao) {
		super();
		this.exerciseDao = exerciseDao;
	}


	/**
	 * Este metodo recupera los workouts sin comsultar sus ejercicios
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public List<Workout> obtenerTodos() throws ExecutionException, InterruptedException, IOException{
		
		QuerySnapshot resultado = DBconnect.getFirestore().collection("workouts").get().get();
		
		List<Workout> workouts = new ArrayList<>();
		
		for(QueryDocumentSnapshot documento: resultado.getDocuments()) {
			Workout workout = documento.toObject(Workout.class);
			workout.setId(documento.getId());
			workouts.add(workout);
		}
		
		return workouts;
	}
	
	/**
	 * Este metodo recupera los workouts consultando sus colecciones de ejercicios, y sus colecciones de series
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws IOException
	 */
	public List<Workout> obtenerTodosCompleto() throws InterruptedException, ExecutionException, IOException{
		
		List<Workout> workouts = obtenerTodos();
		
		for (Workout workout: workouts) {
			List<Exercise> ejercicios = exerciseDao.obtenerPorWorkoutCompleto(workout.getId());
			workout.setEjercicios(ejercicios);
		}
		
		return workouts;
	}

}
