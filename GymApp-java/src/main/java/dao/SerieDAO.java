package dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import service.DBconnect;
import model.Serie;

public class SerieDAO {
	
	/**
	 * Este metodo recupera una coleccion de Series utilizan los identificadores de workout y ejercicio asociados a dicha serie 
	 * @param workoutId
	 * @param ejercicioId
	 * @return
	 * @throws InterruptionException
	 * @throws ExectionException
	 */
	public List<Serie> obtenerPorEjercicio(String workoutId, String ejercicioId) throws InterruptedException, ExecutionException, IOException {
		
		QuerySnapshot resultado = DBconnect.getFirestore()
											.collection("workouts")
											.document(workoutId)
											.collection("exercises")
											.document(ejercicioId)
											.collection("series")
											.get()
											.get();
		
		List<Serie> series = new ArrayList<>();
		
		for (QueryDocumentSnapshot documento: resultado.getDocuments()) {
			Serie serie = documento.toObject(Serie.class);
			serie.setId(documento.getId());
			series.add(serie);
		}
		
		return series;
		
	}

}
