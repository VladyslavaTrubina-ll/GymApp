
package service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Query;

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

    /**
     * Obtiene todos los niveles disponibles para un usuario en concreto
     * @param uid es el id del usuario a consultar
     * @return un list de workouts para poder usarlo como json
     */
    public List<Workout> getAvailableWorkouts(String uid) {

        try {
            // Obtenemos la conexión con la base de datos Firestore
            Firestore db = DBconnect.getFirestore();

            // Buscamos el documento del usuario mediante su UID
            DocumentSnapshot userDocument = db.collection("users")
                    .document(uid)
                    .get()
                    .get();

            // Si el uid recibido no existe
            if (!userDocument.exists()) {
                throw new IllegalStateException("User profile was not found.");
            }

            // Recuperamos el nivel del usuario almacenado en Firestore
            Long levelValue = userDocument.getLong("level");

            // Convertimos el nivel de Long a int
            // Si no tiene un nivel asignado, utilizamos el nivel 0 por defecto
            int userLevel = levelValue != null ? levelValue.intValue() : 0;

            // Objeto QuerySnapshot para guardar lo que devuelva Firebase
            QuerySnapshot snapshot;

            // Si el nivel del usuario es 0 se pedirán workouts de nivel 0
            if (userLevel == 0) {
                snapshot = db.collection("workouts")
                        .whereEqualTo("nivel", 0)
                        .get()
                        .get();

            } else {
                // Consultamos los workouts cuyo nivel sea menor o igual al del usuario
                // Los ordenamos de menor a mayor nivel
                snapshot = db.collection("workouts")
                        .whereLessThanOrEqualTo("nivel", userLevel)
                        .orderBy("nivel")
                        .get()
                        .get();
            }

            // Creamos una lista vacía para almacenar los workouts recuperados
            List<Workout> workouts = new ArrayList<>();

            // Recorremos todos los documentos que devuelve la consulta
            for (QueryDocumentSnapshot document : snapshot.getDocuments()) {

                // Creamos un objeto Workout por cada documento de Firestore
                Workout workout = new Workout();

                // Recuperamos los datos del documento y los asignamos al objeto
                workout.setId(document.getId());
                workout.setNombre(document.getString("nombre"));
                workout.setVideo(document.getString("video"));
                workout.setDescripcion(document.getString("descripcion"));

                // Recuperamos el nivel del workout
                Long level = document.getLong("nivel");

                // Convertimos el nivel a int; si es null, asignamos 0
                workout.setNivel(level != null ? level.intValue() : 0);

                // Añadimos el workout a nuestra lista
                workouts.add(workout);
            }

            // Devolvemos la lista de workouts obtenida
            return workouts;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Workout loading was interrupted.", e);

        } catch (ExecutionException | IOException e) {
            throw new RuntimeException("Could not load workouts from Firestore.", e);
        }
    }

    /**
     * Obtiene todos los detalles de un workout concreto,
     * incluyendo sus ejercicios y las series de cada ejercicio.
     *
     * @param workoutId identificador del workout que queremos consultar
     * @return objeto Workout con toda su información
     */
    public Workout getWorkoutDetails(String workoutId) {

        try {
            // Obtenemos la conexión con Firestore
            Firestore db = DBconnect.getFirestore();

            // Buscamos el documento del workout mediante su ID
            DocumentSnapshot workoutDocument = db.collection("workouts")
                    .document(workoutId)
                    .get()
                    .get();

            // Comprobamos que el workout exista en la base de datos
            if (!workoutDocument.exists()) {
                throw new IllegalStateException("Workout was not found: " + workoutId);
            }

            // Creamos un objeto Workout para almacenar los datos recuperados
            Workout workout = new Workout();

            // Asignamos al objeto los datos principales del workout
            workout.setId(workoutId);
            workout.setNombre(workoutDocument.getString("nombre"));
            workout.setVideo(workoutDocument.getString("video"));
            workout.setDescripcion(workoutDocument.getString("descripcion"));

            // Recuperamos el nivel del workout
            Long level = workoutDocument.getLong("nivel");

            // Convertimos el nivel a int; si no existe, asignamos 0
            workout.setNivel(level != null ? level.intValue() : 0);

            // Creamos una lista para almacenar los ejercicios del workout
            ArrayList<Exercise> exercises = new ArrayList<>();

            // Accedemos a la subcolección exercises del workout
            QuerySnapshot exerciseSnapshot = workoutDocument.getReference()
                    .collection("exercises")
                    .get()
                    .get();

            // Recorremos todos los ejercicios recuperados
            for (QueryDocumentSnapshot exerciseDocument : exerciseSnapshot.getDocuments()) {

                // Creamos un objeto Exercise para cada documento
                Exercise exercise = new Exercise();

                // Recuperamos el nombre y la descripción del ejercicio
                exercise.setNombre(exerciseDocument.getString("nombre"));
                exercise.setDescripcion(exerciseDocument.getString("descripcion"));

                // Creamos una lista para almacenar las series del ejercicio
                ArrayList<Serie> seriesList = new ArrayList<>();

                // Accedemos a la subcolección series de cada ejercicio
                QuerySnapshot seriesSnapshot = exerciseDocument.getReference()
                        .collection("series")
                        .get()
                        .get();

                // Recorremos todas las series recuperadas
                for (QueryDocumentSnapshot seriesDocument : seriesSnapshot.getDocuments()) {

                    // Creamos un objeto Serie para cada documento
                    Serie serie = new Serie();

                    // Recuperamos el nombre y la fotografía de la serie
                    serie.setNombre(seriesDocument.getString("nombre"));
                    serie.setFoto(seriesDocument.getString("foto"));

                    // Recuperamos las repeticiones, duración y descanso
                    Long repetitions = seriesDocument.getLong("repeticiones");
                    Long duration = seriesDocument.getLong("duracion");
                    Long rest = seriesDocument.getLong("descanso");

                    // Convertimos los valores a int
                    // Si alguno no está definido, utilizamos 0
                    serie.setRepeticiones(repetitions != null ? repetitions.intValue() : 0);
                    serie.setDuracion(duration != null ? duration.intValue() : 0);
                    serie.setDescanso(rest != null ? rest.intValue() : 0);

                    // Añadimos la serie a la lista de series del ejercicio
                    seriesList.add(serie);
                }

                // Asignamos las series recuperadas al ejercicio
                exercise.setSeries(seriesList);

                // Añadimos el ejercicio a la lista de ejercicios del workout
                exercises.add(exercise);
            }

            // Asignamos todos los ejercicios al workout
            workout.setEjercicios(exercises);

            // Devolvemos el workout con todos sus datos, ejercicios y series
            return workout;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Workout loading was interrupted.", e);

        } catch (ExecutionException | IOException e) {
            throw new RuntimeException("Could not load workout details from Firestore.", e);
        }
    }

    /**
     * Recupera el historial pendiente de un workout o crea uno nuevo.
     *
     * @param uid identificador del usuario
     * @param workoutId identificador del workout
     * @param estimatedTime tiempo estimado del workout
     * @return ID del documento del historial
     */
    public String startWorkoutHistory(String uid, String workoutId, int estimatedTime) {

        try {
            // Obtenemos la conexión con Firestore
            Firestore db = DBconnect.getFirestore();

            // Accedemos al historial del usuario
            CollectionReference historyCollection = db.collection("users")
                    .document(uid)
                    .collection("workoutHistory");

            // Recuperamos los registros existentes
            QuerySnapshot snapshot = historyCollection.get().get();

            QueryDocumentSnapshot unfinishedHistory = null;
            Date latestDate = null;

            // Buscamos el registro más reciente del mismo workout que no esté terminado
            for (QueryDocumentSnapshot document : snapshot.getDocuments()) {

                String savedWorkoutId = document.getString("workoutID");
                Long completionPercentage = document.getLong("completionPercentage");

                if (workoutId.equals(savedWorkoutId)
                        && (completionPercentage == null || completionPercentage < 100)) {

                    Date historyDate = document.getDate("date");

                    if (unfinishedHistory == null
                            || (historyDate != null
                            && (latestDate == null || historyDate.after(latestDate)))) {

                        unfinishedHistory = document;
                        latestDate = historyDate;
                    }
                }
            }

            // Si existe un registro pendiente, reutilizamos su ID
            if (unfinishedHistory != null) {
                return unfinishedHistory.getId();
            }

            // Si no existe, creamos un registro nuevo
            Map<String, Object> history = new HashMap<>();

            history.put("workoutID", workoutId);
            history.put("date", new Date());
            history.put("estimatedTime", estimatedTime);
            history.put("totalTime", 0);
            history.put("completionPercentage", 0);

            // Creamos el documento en Firestore
            DocumentReference historyRef = historyCollection.add(history).get();

            // Devolvemos el ID generado automáticamente
            return historyRef.getId();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Workout history creation was interrupted.", e);

        } catch (ExecutionException | IOException e) {
            throw new RuntimeException("Could not create or retrieve workout history.", e);
        }
    }

    /**
     * Recupera el progreso guardado de un registro del historial.
     *
     * @param uid identificador del usuario
     * @param historyId identificador del registro
     * @return datos guardados del entrenamiento
     */
    /**
     * Recupera el progreso guardado de un registro del historial.
     *
     * @param uid identificador del usuario
     * @param historyId identificador del registro
     * @return datos guardados del entrenamiento
     */
    public Map<String, Object> getWorkoutHistoryProgress(String uid, String historyId) {

        try {
            Firestore db = DBconnect.getFirestore();

            DocumentSnapshot document = db.collection("users")
                    .document(uid)
                    .collection("workoutHistory")
                    .document(historyId)
                    .get()
                    .get();

            if (!document.exists()) {
                throw new IllegalStateException("Workout history was not found.");
            }

            Map<String, Object> progress = new HashMap<>();

            Long totalTime = document.getLong("totalTime");
            Long completionPercentage = document.getLong("completionPercentage");

            progress.put("totalTime", totalTime != null ? totalTime : 0);
            progress.put("completionPercentage",
                    completionPercentage != null ? completionPercentage : 0);

            return progress;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Workout history loading was interrupted.", e);

        } catch (ExecutionException | IOException e) {
            throw new RuntimeException("Could not load workout history progress.", e);
        }
    }

    /**
     * Actualiza el tiempo, el porcentaje y los ejercicios completados.
     */
    /**
     * Actualiza el tiempo y el porcentaje de finalización.
     */
    public void updateWorkoutHistory(
            String uid,
            String historyId,
            int totalTime,
            int completionPercentage) {

        try {
            Firestore db = DBconnect.getFirestore();

            Map<String, Object> updates = new HashMap<>();

            // Guardamos el tiempo total empleado
            updates.put("totalTime", totalTime);

            // Limitamos el porcentaje entre 0 y 100
            updates.put("completionPercentage",
                    Math.max(0, Math.min(100, completionPercentage)));

            db.collection("users")
                    .document(uid)
                    .collection("workoutHistory")
                    .document(historyId)
                    .update(updates)
                    .get();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Workout history update was interrupted.", e);

        } catch (ExecutionException | IOException e) {
            throw new RuntimeException("Could not update workout history.", e);
        }
    }

    /**
     * Obtiene el historial completo de un usuario.
     *
     * @param uid identificador del usuario
     * @return lista de registros del historial
     */
    public List<Map<String, Object>> getWorkoutHistory(String uid) {

        try {
            // Obtenemos la conexión con Firestore
            Firestore db = DBconnect.getFirestore();

            // Consultamos el historial del usuario, del más reciente al más antiguo
            QuerySnapshot snapshot = db.collection("users")
                    .document(uid)
                    .collection("workoutHistory")
                    .orderBy("date", Query.Direction.DESCENDING)
                    .get()
                    .get();

            // Creamos una lista para almacenar los registros del historial
            List<Map<String, Object>> historyList = new ArrayList<>();

            // Recorremos los documentos del historial
            for (QueryDocumentSnapshot document : snapshot.getDocuments()) {

                // Recuperamos el ID del workout asociado al registro
                String workoutId = document.getString("workoutID");

                // Buscamos el nombre del workout para mostrarlo en HomeView
                String workoutName = "Workout no disponible";

                if (workoutId != null && !workoutId.isBlank()) {

                    DocumentSnapshot workoutDocument = db.collection("workouts")
                            .document(workoutId)
                            .get()
                            .get();

                    if (workoutDocument.exists()
                            && workoutDocument.getString("nombre") != null) {
                        workoutName = workoutDocument.getString("nombre");
                    }
                }

                // Creamos un Map con los datos que necesita la interfaz
                Map<String, Object> history = new HashMap<>();

                history.put("historyId", document.getId());
                history.put("workoutID", workoutId);
                history.put("workoutName", workoutName);
                history.put("date", document.getDate("date"));

                Long estimatedTime = document.getLong("estimatedTime");
                Long totalTime = document.getLong("totalTime");
                Long completionPercentage = document.getLong("completionPercentage");

                history.put("estimatedTime", estimatedTime != null ? estimatedTime : 0);
                history.put("totalTime", totalTime != null ? totalTime : 0);
                history.put("completionPercentage",
                        completionPercentage != null ? completionPercentage : 0);

                historyList.add(history);
            }

            // Devolvemos el historial recuperado
            return historyList;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Workout history loading was interrupted.", e);

        } catch (ExecutionException | IOException e) {
            throw new RuntimeException("Could not load workout history from Firestore.", e);
        }
    }
}
