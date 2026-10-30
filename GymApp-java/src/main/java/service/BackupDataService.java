package service;

import java.util.List;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import model.Exercise;
import model.Serie;
import model.Workout;
import service.DBconnect;

public class BackupDat{
	
	//	PARA EL MAPEO DE LA BD EN FIREBASE
	public List<Workout> obtenerWorkouts() throws Exception{
		//	leo los workouts
		QuerySnapshot workoutsSnapshot = DBconnect.getFirestore().collection("workouts").get().get();
		
		for(QueryDocumentSnapshot docWorkout: workoutsSnapshot){
			Workout workout = docWorkout.toObject(Workout.class);
			String idWorkout = docWorkout.getId();
			
		}
		
		QuerySnapshot exerciseSnapshot = DBconnect.getFirestore().collection("exercises").get().get();
		
		for(QueryDocumentSnapshot docExercise: exerciseSnapshot) {
			Exercise exercise = docExercise.toObject(Exercise.class);
			String idExercise = docExercise.getId();
		}
		
		QuerySnapshot serieSnapshot = DBconnect.getFirestore().collection("series").get().get();
		
		for(QueryDocumentSnapshot docSerie: serieSnapshot) {
			Serie serie = docSerie.toObject(Serie.class);
			String idSerie = docSerie.getId();
		}
	}
	

}
