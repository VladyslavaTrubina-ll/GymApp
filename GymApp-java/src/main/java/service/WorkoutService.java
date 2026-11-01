package service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.DocumentReference;

import model.Exercise;
import model.Serie;
import model.Workout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class WorkoutService {

    public List<QueryDocumentSnapshot> getAvailableWorkouts(String uid) {

        try {
            Firestore db = DBconnect.getFirestore();

            DocumentSnapshot userDocument = db.collection("users")
                    .document(uid)
                    .get()
                    .get();

            if (!userDocument.exists()) {
                throw new IllegalStateException(
                        "User profile was not found."
                );
            }

            Long levelValue = userDocument.getLong("level");

            int userLevel = levelValue != null
                    ? levelValue.intValue() : 0;

            if (userLevel == 0) {
                return db.collection("workouts")
                        .whereEqualTo("nivel", 0)
                        .get()
                        .get()
                        .getDocuments();
            }

            return db.collection("workouts")
                    .whereLessThanOrEqualTo("nivel", userLevel)
                    .orderBy("nivel")
                    .get()
                    .get()
                    .getDocuments();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(
                    "Workout loading was interrupted.", e
            );

        } catch (ExecutionException | IOException e) {
            throw new RuntimeException(
                    "Could not load workouts from Firestore.", e
            );
        }
    }

    public Workout getWorkoutDetails(String workoutId) {

        try {
            Firestore db = DBconnect.getFirestore();

            DocumentSnapshot workoutDocument = db.collection("workouts")
                    .document(workoutId)
                    .get()
                    .get();

            if (!workoutDocument.exists()) {
                throw new IllegalStateException(
                        "Workout was not found: " + workoutId
                );
            }

            Workout workout = new Workout();

            workout.setId(workoutId);
            workout.setNombre(workoutDocument.getString("nombre"));
            workout.setVideo(workoutDocument.getString("video"));
            workout.setDescripcion(
                    workoutDocument.getString("descripcion")
            );

            Long level = workoutDocument.getLong("nivel");

            workout.setLevel(level != null ? level.intValue() : 0);

            ArrayList<Exercise> exercises = new ArrayList<>();

            QuerySnapshot exerciseSnapshot = workoutDocument.getReference()
                    .collection("exercises")
                    .get()
                    .get();

            for (QueryDocumentSnapshot exerciseDocument :
                    exerciseSnapshot.getDocuments()) {

                Exercise exercise = new Exercise();

                exercise.setNombre(
                        exerciseDocument.getString("nombre")
                );

                exercise.setDescripcion(
                        exerciseDocument.getString("descripcion")
                );

                ArrayList<Serie> seriesList = new ArrayList<>();

                QuerySnapshot seriesSnapshot = exerciseDocument.getReference()
                        .collection("series")
                        .get()
                        .get();

                for (QueryDocumentSnapshot seriesDocument :
                        seriesSnapshot.getDocuments()) {

                    Serie serie = new Serie();

                    serie.setNombre(
                            seriesDocument.getString("nombre")
                    );

                    serie.setFoto(
                            seriesDocument.getString("foto")
                    );

                    Long repetitions =
                            seriesDocument.getLong("repeticiones");

                    Long duration =
                            seriesDocument.getLong("duracion");

                    Long rest =
                            seriesDocument.getLong("descanso");

                    serie.setRepeticiones(
                            repetitions != null ? repetitions.intValue() : 0
                    );

                    serie.setDuracion(
                            duration != null ? duration.intValue() : 0
                    );

                    serie.setDescanso(
                            rest != null ? rest.intValue() : 0
                    );

                    seriesList.add(serie);
                }

                exercise.setSeries(seriesList);
                exercises.add(exercise);
            }

            workout.setEjercicios(exercises);

            return workout;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "Workout loading was interrupted.", e
            );

        } catch (ExecutionException | IOException e) {

            throw new RuntimeException(
                    "Could not load workout details from Firestore.", e
            );
        }
    }

    // =====================================================
    // WORKOUT HISTORY
    // =====================================================

    public String startWorkoutHistory(
            String uid,
            String workoutId,
            int estimatedTime) {

        try {
            Firestore db = DBconnect.getFirestore();

            Map<String, Object> history = new HashMap<>();

            history.put("workoutID", workoutId);
            history.put("date", new Date());
            history.put("estimatedTime", estimatedTime);
            history.put("totalTime", 0);
            history.put("completionPercentage", 0);

            DocumentReference historyRef = db.collection("users")
                    .document(uid)
                    .collection("workoutHistory")
                    .add(history)
                    .get();

            return historyRef.getId();

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "Workout history creation was interrupted.", e
            );

        } catch (ExecutionException | IOException e) {

            throw new RuntimeException(
                    "Could not create workout history.", e
            );
        }
    }

    public void updateWorkoutHistory(
            String uid,
            String historyId,
            int totalTime,
            int completionPercentage) {

        try {
            Firestore db = DBconnect.getFirestore();

            Map<String, Object> updates = new HashMap<>();

            updates.put("totalTime", totalTime);

            updates.put(
                    "completionPercentage",
                    Math.max(0, Math.min(100, completionPercentage))
            );

            db.collection("users")
                    .document(uid)
                    .collection("workoutHistory")
                    .document(historyId)
                    .update(updates)
                    .get();

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "Workout history update was interrupted.", e
            );

        } catch (ExecutionException | IOException e) {

            throw new RuntimeException(
                    "Could not update workout history.", e
            );
        }
    }
}