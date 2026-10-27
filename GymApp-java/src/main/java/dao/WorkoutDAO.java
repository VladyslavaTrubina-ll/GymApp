package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.QuerySnapshot;

import model.Workout;
import service.DBconnect;

public class WorkoutDAO {
	
	
	
	public List<Workout> obtenerTodosWorkouts() throws ExecutionException, InterruptedException{
		
		List<Workout> workouts = new ArrayList<>();
		
		QuerySnapshot workoutsSnapshot = DBconnect.getFirestore().collection("workouts").get().get();
	}

}
