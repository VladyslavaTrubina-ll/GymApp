package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
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

        try{
        	
        	ServerSocket serverSocket= new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT);

            while (true) {
                Socket client = serverSocket.accept();

                System.out.println(
                        "Client connected: " + client.getInetAddress());
                // listens each client in a separate thread
                //new Thread(() -> handleClient(client)).start();
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
            // Read one JSON request per line
            String requestJson = input.readLine();

            if (requestJson == null) {
                return;
            }

            System.out.println("Received JSON: " + requestJson);

         // Convert JSON into a JsonObject
         JsonObject jsonObject = gson.fromJson(requestJson, JsonObject.class);

         // Get the request type
         String action = jsonObject.get("action").getAsString();

         // Remove action because it is not a field in User
         jsonObject.remove("action");
         
         // Extract the User object
         User user = gson.fromJson(jsonObject, User.class);

         // Now use the action to decide what to do
         if ("login".equals(action)) {
        	 
             UserController controller = new UserController();

             Map<String, Object> result = controller.login(
                 user.getEmail(),
                 user.getPassword()
             );

             output.println("{\"success\":true,\"message\":\"Login successful\"}");
         }
        } catch (Exception e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }

    private static Map<String, Object> processRequest(
            Map<String, String> request) {

        Map<String, Object> response = new HashMap<>();

        try {
            if (request == null || request.get("action") == null) {
                throw new IllegalArgumentException(
                        "Missing request action.");
            }

            UserController controller = new UserController();

            String action = request.get("action");

            if ("login".equals(action)) {

                // Handle login
                Map<String, Object> result = controller.login(
                        request.get("email"),
                        request.get("password")
                );

                response.put("success", true);
                response.put("message", "Login successful.");
                response.put("uid", result.get("localId"));

            } else if ("register".equals(action)) {

                // Handle registration
                controller.register(
                        request.get("firstName"),
                        request.get("lastName"),
                        request.get("email"),
                        request.get("password"),
                        request.get("birthDate")
                );

                response.put("success", true);
                response.put("message", "Registration successful.");

            } else {
                throw new IllegalArgumentException(
                        "Unknown action: " + action);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return response;
    }


}
