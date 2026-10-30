package dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import model.Exercise;
import model.Serie;
import service.DBconnect;



public class ExerciseDAO {
	
	private final SerieDAO serieDAO;
	
	
	public ExerciseDAO(SerieDAO serieDAO) {
		this.serieDAO = serieDAO;
	}

	/**
	 * Este metodo recupera los ejercicios sin consultar sus series
	 * @param workoutId
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public List<Exercise> obtenerPorWorkout(String workoutId) throws InterruptedException, ExecutionException, IOException{
		
		QuerySnapshot resultado = DBconnect.getFirestore()
											.collection("workouts").document(workoutId).collection("exercises").get().get();
		
		List<Exercise> ejercicios = new ArrayList<>();
		
		for (QueryDocumentSnapshot documento: resultado.getDocuments()) {
			
			Exercise ejercicio = documento.toObject(Exercise.class);
			ejercicio.setId(documento.getId());
			ejercicios.add(ejercicio);
		}
		return ejercicios;
	}
	
	/**
	 * Este metodo recupera los ejercicios y para cada ejercicio consulta sus series usando SerieDAO
	 * @param workoutId
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public List<Exercise> obtenerPorWorkoutCompleto(String workoutId) throws InterruptedException, ExecutionException, IOException{
		
		List<Exercise> ejercicios = obtenerPorWorkout(workoutId);
		
		for (Exercise ejercicio: ejercicios) {
			List<Serie> series = serieDAO.obtenerPorEjercicio(workoutId, ejercicio.getId());
			ejercicio.setSeries(series);
		}
		return ejercicios;
	}

}
