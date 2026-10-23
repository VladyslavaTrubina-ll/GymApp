
package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import controller.UserController;
import model.User;

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
            // 1. lee el jason que le viene por INput
            String requestJson = input.readLine();

            if (requestJson == null || requestJson.isBlank()) {
                throw new IllegalArgumentException(
                        "Empty request received.");
            }

            System.out.println("Received JSON from client: " + requestJson);

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

            //se guarda el tipo de action
            String action = jsonObject.get("action").getAsString();

            //se quita del objeto
            jsonObject.remove("action");

            // se "vuelca" el objeto a uno de tipo User
            User user = gson.fromJson(jsonObject, User.class);

            // Si la accion que le viene en el request fue login llama a su controlador
            if ("login".equals(action)) {

                UserController controller = new UserController();

                //obtiene el resultado del login
                Map<String, Object> result = controller.login(
                        user.getEmail(),
                        user.getPassword()
                );

                //se envia directamente el resultado del login al cliente
                String response = gson.toJson(result);

                System.out.println("Login result: " + response);

                output.write(response + "\n");
                output.flush();

            } else {
                JsonObject response = new JsonObject();
                response.addProperty("success", false);
                response.addProperty(
                        "message", "Unknown action: " + action);

                output.write(gson.toJson(response) + "\n");
                output.flush();
            }

        } catch (Exception e) {
            System.err.println("Client error: " + e.getMessage());

            // Send an error response to the client
            try {
                JsonObject errorResponse = new JsonObject();
                errorResponse.addProperty("success", false);
                errorResponse.addProperty(
                        "message",
                        e.getMessage() == null
                                ? "An unexpected server error occurred."
                                : e.getMessage());

                outputErrorResponse(client, errorResponse);
            } catch (Exception ignored) {
                // Nothing else to do.
            }
        }
    }

    private static void outputErrorResponse(
            Socket client, JsonObject errorResponse) throws Exception {

        PrintWriter output = new PrintWriter(
                client.getOutputStream(), true);

        output.write(gson.toJson(errorResponse) + "\n");
        output.flush();
    }
}
