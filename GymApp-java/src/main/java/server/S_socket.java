
package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import controller.UserController;
import model.User;
import model.Workout;
import service.WorkoutService;
import service.UserService;

public class S_socket {

    private static final int PORT = 5000;
    private static final Gson gson = new Gson();

    public static void main(String[] args) {

        System.out.println("Starting GymApp server...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Server listening on port " + PORT);

            while (true) {

                Socket client = serverSocket.accept();

                System.out.println(
                        "Client connected: " + client.getInetAddress());

                handleClient(client);
            }

        } catch (Exception e) {

            System.err.println("Server error: " + e.getMessage());

        }
    }

    private static void handleClient(Socket client) {

        try (
            Socket socket = client;
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(
                    socket.getOutputStream(), true)
        ) {

            // Se queda escuchando request del mismo cliente
            String requestJson;

            while ((requestJson = input.readLine()) != null) {

                try {

                    // 1. lee el jason que le viene por INput
                    if (requestJson.isBlank()) {
                        throw new IllegalArgumentException(
                                "Empty request received.");
                    }

                 // Muestra la petición JSON recibida del cliente
                    System.out.println("Petición recibida: " + requestJson);

                    // 2. Convierte en un objeto
                    JsonObject jsonObject =
                            gson.fromJson(requestJson, JsonObject.class);

                    if (jsonObject == null) {
                        throw new IllegalArgumentException(
                                "Invalid JSON request.");
                    }

                    // 3. obtiene el tipo de request que le viene en el json
                    if (!jsonObject.has("action")
                            || jsonObject.get("action").isJsonNull()) {
                        throw new IllegalArgumentException(
                                "Missing request action.");
                    }

                    //4. se guarda el tipo de accion
                    String action = jsonObject.get("action").getAsString();

                    //5. se quita del objeto
                    jsonObject.remove("action");

                    //6. evalua el tipo de accion del request y llama a su controlador indicado
                    if ("login".equals(action)) {

                        // se "vuelca" el objeto a uno de tipo User
                        User user = gson.fromJson(jsonObject, User.class);

                        UserController controller = new UserController();

                        //obtiene el resultado del login
                        Map<String, Object> result = controller.login(
                                user.getEmail(),
                                user.getPassword()
                        );

                        //se envia directamente el resultado del login al cliente
                        String response = gson.toJson(result);

                        System.out.println("Login result: " + response);

                        output.println(response);
                        output.flush();

                    }else if (action.equals("updateUserData")) {

					    // Obtiene los datos enviados por HomeView
					    String uid = jsonObject.get("uid").getAsString();
					    String firstName = jsonObject.get("firstName").getAsString();
					    String lastName = jsonObject.get("lastName").getAsString();
					    String email = jsonObject.get("email").getAsString();
					    String birthDate = jsonObject.get("birthDate").getAsString();
					
					    // Actualiza los datos del usuario en Firestore
					    UserService userService = new UserService();
					
					    userService.updateUserProfileAsync(
					        uid, firstName, lastName, email, birthDate
					    ).get();
					
					    // Prepara la respuesta
					    JsonObject result = new JsonObject();
					    result.addProperty("success", true);
					    result.addProperty("message", "Datos actualizados correctamente.");
					
					    // Muestra la respuesta enviada
					    System.out.println("Respuesta enviada: " + gson.toJson(result));
					
					    // Envía la respuesta a HomeView
					    output.println(gson.toJson(result));
					    output.flush();
					    
					} else if ("getAvailableWorkouts".equals(action)) {

                        // Obtiene el UID enviado desde HomeView
                        if (!jsonObject.has("uid")
                                || jsonObject.get("uid").isJsonNull()) {
                            throw new IllegalArgumentException(
                                    "Missing user UID.");
                        }

                        String uid = jsonObject.get("uid").getAsString();

                        WorkoutService workoutService = new WorkoutService();

                        // Obtiene los workouts disponibles para ese usuario
                        List<Workout> workouts =
                                workoutService.getAvailableWorkouts(uid);

                        // Prepara la respuesta
                        Map<String, Object> result = new HashMap<>();
                        result.put("success", true);
                        result.put("workouts", workouts);
                        
                        // Muestra la respuesta que se va a enviar
                        System.out.println("Respuesta enviada: " + gson.toJson(result));

                        // Convierte el resultado a JSON
                        String response = gson.toJson(result);

                        // Envía la respuesta a HomeView
                        output.println(response);
                        output.flush();

                    }else if ("getUserData".equals(action)) {

                        //  UID enviado desde HomeView
                        String uid = jsonObject.get("uid").getAsString();

                        UserService userService = new UserService();

                        // Obtiene los datos del usuario desde Firestore
                        DocumentSnapshot document =
                                userService.getUserProfileAsync(uid);

                        // Prepara la respuesta
                        JsonObject result = new JsonObject();

                        if (document.exists()) {

                            result.addProperty("success", true);
                            result.addProperty("firstName", document.getString("firstName"));
                            result.addProperty("lastName", document.getString("lastName"));
                            result.addProperty("email", document.getString("email"));
                            result.addProperty("birthDate", document.getString("birthDate"));

                            Long level = document.getLong("level");

                            result.addProperty("level", level != null ? level : 0);

                        } else {

                            result.addProperty("success", false);
                            result.addProperty("message", "User profile not found.");
                        }

                        // Muestra la respuesta que se va a enviar
                        System.out.println("Respuesta enviada: " + gson.toJson(result));
                        
                        // Convierte el resultado a JSON
                        String response = gson.toJson(result);

                        // Envía la respuesta a HomeView
                        output.println(response);
                        output.flush();
                    }else if ("getWorkoutDetails".equals(action)) {

                        // Obtiene el ID del workout enviado desde HomeView
                        if (!jsonObject.has("workoutId")
                                || jsonObject.get("workoutId").isJsonNull()) {
                            throw new IllegalArgumentException("Missing workout ID.");
                        }

                        String workoutId = jsonObject.get("workoutId").getAsString();

                        WorkoutService workoutService = new WorkoutService();

                        // Obtiene los detalles del workout seleccionado
                        Workout workout = workoutService.getWorkoutDetails(workoutId);

                        // Prepara la respuesta
                        Map<String, Object> result = new HashMap<>();
                        result.put("success", true);
                        result.put("workout", workout);
                        
                        // Muestra la respuesta que se va a enviar
                        System.out.println("Respuesta enviada: " + gson.toJson(result));

                        // Convierte el resultado a JSON
                        String response = gson.toJson(result);

                        // Envía la respuesta a WorkoutDetailView
                        output.println(response);
                        output.flush();
                    }else if ("startWorkoutHistory".equals(action)) {

                        // Obtiene los datos enviados desde WorkoutSession
                        String uid = jsonObject.get("uid").getAsString();
                        String workoutId = jsonObject.get("workoutId").getAsString();
                        int estimatedTime = jsonObject.get("estimatedTime").getAsInt();

                        WorkoutService workoutService = new WorkoutService();

                        // Crea el registro del workout en Firestore
                        String historyId = workoutService.startWorkoutHistory(
                                uid, workoutId, estimatedTime
                        );

                        // Recupera el progreso guardado para continuar el entrenamiento
                        Map<String, Object> progress =
                                workoutService.getWorkoutHistoryProgress(uid, historyId);

                        // Prepara la respuesta con el ID y el progreso anterior
                        JsonObject result = new JsonObject();
                        result.addProperty("success", true);
                        result.addProperty("historyId", historyId);
                        result.addProperty("totalTime",
                                ((Number) progress.get("totalTime")).intValue());
                        result.addProperty("completionPercentage",
                                ((Number) progress.get("completionPercentage")).intValue());
                        
                        
                        // Muestra la respuesta que se va a enviar
                        System.out.println("Respuesta enviada: " + gson.toJson(result));

                        // Envía la respuesta a WorkoutSession
                        output.println(gson.toJson(result));
                        output.flush();
                    }else if ("updateWorkoutHistory".equals(action)) {

                        // Obtiene los datos enviados desde WorkoutSession
                        String uid = jsonObject.get("uid").getAsString();
                        String historyId = jsonObject.get("historyId").getAsString();
                        int totalTime = jsonObject.get("totalTime").getAsInt();
                        int percentage = jsonObject.get("completionPercentage").getAsInt();

                        List<String> completedExerciseKeys = new java.util.ArrayList<>();
                        if (jsonObject.has("completedExerciseKeys")
                                && jsonObject.get("completedExerciseKeys").isJsonArray()) {
                            for (com.google.gson.JsonElement key :
                                    jsonObject.getAsJsonArray("completedExerciseKeys")) {
                                completedExerciseKeys.add(key.getAsString());
                            }
                        }

                        WorkoutService workoutService = new WorkoutService();

                        // Actualiza el tiempo, el porcentaje y los ejercicios completados
                        workoutService.updateWorkoutHistory(
                                uid, historyId, totalTime, percentage
                        );

                        // Prepara la respuesta
                        JsonObject result = new JsonObject();
                        result.addProperty("success", true);

                     // Muestra la respuesta que se va a enviar
                        System.out.println("Respuesta enviada: " + gson.toJson(result));
                        
                        // Envía la respuesta a WorkoutSession
                        output.println(gson.toJson(result));
                        output.flush();
                    } else if ("getWorkoutHistory".equals(action)) {

                        // Obtiene el UID enviado desde HomeView
                        if (!jsonObject.has("uid")
                                || jsonObject.get("uid").isJsonNull()) {
                            throw new IllegalArgumentException("Missing user UID.");
                        }

                        String uid = jsonObject.get("uid").getAsString();

                        WorkoutService workoutService = new WorkoutService();

                        // Recupera el historial del usuario desde Firestore
                        List<Map<String, Object>> history =
                                workoutService.getWorkoutHistory(uid);

                        // Prepara la respuesta para HomeView
                        Map<String, Object> result = new HashMap<>();
                        result.put("success", true);
                        result.put("history", history);

                        // Envía el historial al cliente
                        output.println(gson.toJson(result));
                        output.flush();

                    } else {

                        JsonObject response = new JsonObject();

                        response.addProperty("success", false);
                        response.addProperty(
                                "message", "Unknown action: " + action);

                        output.println(gson.toJson(response));
                        output.flush();
                    }

                } catch (Exception e) {

                    System.err.println("Client request error: " + e.getMessage());

                    // Send an error response to the client
                    JsonObject errorResponse = new JsonObject();

                    errorResponse.addProperty("success", false);
                    errorResponse.addProperty(
                            "message",
                            e.getMessage() == null
                                    ? "An unexpected server error occurred."
                                    : e.getMessage());

                    output.println(gson.toJson(errorResponse));
                    output.flush();
                }
            }

        } catch (Exception e) {

            System.err.println("Client connection error: " + e.getMessage());

        }
    }
}
