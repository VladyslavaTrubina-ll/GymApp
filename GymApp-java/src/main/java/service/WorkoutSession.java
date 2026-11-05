package service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WorkoutSession {

    // Información del usuario y del entrenamiento
    private String userId;
    private String workoutId;

    // Duración estimada del entrenamiento y número total de ejercicios
    private int estimatedTime;
    private int totalExercises;

    // Flujos utilizados para comunicarse con el servidor
    private PrintWriter output;
    private BufferedReader input;

    // Gson permite convertir objetos Java a JSON y viceversa
    private Gson gson = new Gson();

    // Identificador del documento del historial creado en Firestore
    private String historyId;

    // Tiempo total de ejercicio, medido en segundos
    private int totalTime;

    // Guarda cuántas series se han completado en cada ejercicio
    private Map<String, Integer> completedSeries = new HashMap<>();

    // Guarda los identificadores de los ejercicios que se han completado
    private Set<String> completedExerciseKeys = new HashSet<>();

    /**
     * Crea una nueva sesión de entrenamiento
     *
     * @param userId Identificador del usuario que ha iniciado sesión
     * @param workoutId Identificador del entrenamiento seleccionado
     * @param estimatedTime Duración estimada del entrenamiento en segundos
     * @param totalExercises Número total de ejercicios
     * @param output Flujo utilizado para enviar peticiones al servidor
     * @param input Flujo utilizado para recibir respuestas del servidor
     */
    public WorkoutSession(String userId, String workoutId, int estimatedTime,
                          int totalExercises, PrintWriter output, BufferedReader input) {

        this.userId = userId;
        this.workoutId = workoutId;
        this.estimatedTime = estimatedTime;
        this.totalExercises = totalExercises;
        this.output = output;
        this.input = input;
    }

    /**
     * Recupera el historial existente o crea uno nuevo si no existe.
     * También restaura el tiempo y los ejercicios completados.
     */
    public synchronized void startIfNeeded() {

        // Evita solicitar dos veces el mismo historial durante esta sesión
        if (historyId != null) {
            return;
        }

        try {
            // Creamos la petición JSON que enviaremos al servidor
            JsonObject request = new JsonObject();

            // Indicamos al servidor qué operación debe realizar
            request.addProperty("action", "startWorkoutHistory");

            // Añadimos los datos necesarios para recuperar o crear el historial
            request.addProperty("uid", userId);
            request.addProperty("workoutId", workoutId);
            request.addProperty("estimatedTime", estimatedTime);

            // Enviamos la petición al servidor
            output.println(gson.toJson(request));
            output.flush();

            // Esperamos la respuesta del servidor
            String response = input.readLine();

            if (response == null) {
                throw new RuntimeException("No se ha recibido respuesta del servidor.");
            }

            // Convertimos la respuesta JSON en un objeto JsonObject
            JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);

            // Comprobamos si el servidor ha recuperado o creado el historial
            if (!jsonResponse.get("success").getAsBoolean()) {
                throw new RuntimeException(jsonResponse.get("message").getAsString());
            }

            // Restauramos el ID y el tiempo guardado
            historyId = jsonResponse.get("historyId").getAsString();
            totalTime = jsonResponse.has("totalTime")
                    ? jsonResponse.get("totalTime").getAsInt() : 0;

            

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al iniciar el historial: " + e.getMessage(), e);
        }
    }

    /**
     * Añade un segundo al tiempo total del entrenamiento.
     * Este método se ejecuta mientras el cronómetro del ejercicio está activo.
     */
    public synchronized void addElapsedSecond() {

        // Solo contabilizamos el tiempo si el entrenamiento ya ha comenzado
        if (historyId != null) {
            totalTime++;
        }
    }

    /**
     * Registra una serie completada y comprueba si el ejercicio ha finalizado.
     *
     * @param exerciseKey Identificador del ejercicio
     * @param seriesKey Identificador de la serie completada
     * @param totalSeriesForExercise Número total de series del ejercicio
     */
    public synchronized void completeSeries(String exerciseKey, String seriesKey,
                                             int totalSeriesForExercise) {

        // Si el ejercicio ya estaba completado antes de cerrar la aplicación,
        // no volvemos a contar sus series.
        if (completedExerciseKeys.contains(exerciseKey)) {
            return;
        }

        // Incrementamos el número de series completadas para este ejercicio
        completedSeries.put(
            exerciseKey,
            completedSeries.getOrDefault(exerciseKey, 0) + 1
        );

        // Comprobamos si se han completado todas las series del ejercicio
        if (completedSeries.get(exerciseKey) >= totalSeriesForExercise) {

            // Marcamos el ejercicio como completado
            completedExerciseKeys.add(exerciseKey);
        }

        // Guardamos el progreso actualizado del entrenamiento
        saveProgress();
    }

    /**
     * Envía el progreso actual del entrenamiento al servidor.
     * El servidor se encarga de actualizar el documento correspondiente en Firestore.
     */
    public synchronized void saveProgress() {

        // No hay nada que guardar si el entrenamiento todavía no ha comenzado
        if (historyId == null) {
            return;
        }

        try {
            // Calculamos el porcentaje de ejercicios completados
            int completionPercentage = 0;

            if (totalExercises > 0) {
                completionPercentage =
                    (completedExerciseKeys.size() * 100) / totalExercises;
            }

            // Preparamos la lista de ejercicios completados para guardarla en Firestore
            List<String> completedKeys = new ArrayList<>(completedExerciseKeys);

            // Creamos la petición JSON para actualizar el historial
            JsonObject request = new JsonObject();

            // Indicamos al servidor qué operación debe realizar
            request.addProperty("action", "updateWorkoutHistory");

            // Añadimos los datos necesarios para actualizar el documento
            request.addProperty("uid", userId);
            request.addProperty("historyId", historyId);
            request.addProperty("totalTime", totalTime);
            request.addProperty("completionPercentage", completionPercentage);
            request.add("completedExerciseKeys", gson.toJsonTree(completedKeys));

            // Enviamos la petición al servidor
            output.println(gson.toJson(request));
            output.flush();

            // Esperamos la confirmación del servidor
            String response = input.readLine();

            if (response == null) {
                throw new RuntimeException("No se ha recibido respuesta del servidor.");
            }

            // Convertimos la respuesta JSON en un objeto JsonObject
            JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);

            // Comprobamos si el servidor ha actualizado correctamente el historial
            if (!jsonResponse.get("success").getAsBoolean()) {
                throw new RuntimeException(jsonResponse.get("message").getAsString());
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al guardar el progreso: " + e.getMessage(), e);
        }
    }

    /**
     * Comprueba si el entrenamiento ha comenzado.
     *
     * @return true si ya se ha creado o recuperado el historial
     */
    public boolean hasStarted() {
        return historyId != null;
    }

    /**
     * Devuelve el tiempo total de ejercicio en segundos.
     */
    public int getTotalTime() {
        return totalTime;
    }

    /**
     * Devuelve el número de ejercicios completados.
     */
    public int getCompletedExercises() {
        return completedExerciseKeys.size();
    }

    /**
     * Devuelve el número total de ejercicios del entrenamiento.
     */
    public int getTotalExercises() {
        return totalExercises;
    }
}
