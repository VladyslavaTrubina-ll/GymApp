package service;


import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import model.User;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class AuthService {

    private final Gson gson = new Gson();

    private final HttpClient httpClient = HttpClient.newHttpClient();

    // REGISTER
    public User register(
            String firstName,
            String lastName,
            String email,
            String password,
            String birthDate) throws Exception {

        // 1. Create the account in Firebase Authentication
        UserRecord.CreateRequest authRequest =
                new UserRecord.CreateRequest()
                        .setEmail(email)
                        .setPassword(password)
                        .setDisplayName(firstName + " " + lastName);

        UserRecord firebaseUser =
                FirebaseAuth.getInstance().createUser(authRequest);

        String uid = firebaseUser.getUid();

        // 2. Create the user profile
        User user = new User(
                uid,
                firstName,
                lastName,
                email,
                birthDate,
                0
        );

        // 3. Save the profile in Firestore
        Map<String, Object> profile = new HashMap<>();
        profile.put("firstName", firstName);
        profile.put("lastName", lastName);
        profile.put("email", email);
        profile.put("birthDate", birthDate);
        profile.put("level", 0);

        try {
            Firestore db = DBconnect.getFirestore();

            db.collection("users")
                    .document(uid)
                    .set(profile)
                    .get();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            try {
                FirebaseAuth.getInstance().deleteUser(uid);
            } catch (Exception cleanupError) {
                e.addSuppressed(cleanupError);
            }

            throw e;

        } catch (ExecutionException | IOException e) {
            try {
                FirebaseAuth.getInstance().deleteUser(uid);
            } catch (Exception cleanupError) {
                e.addSuppressed(cleanupError);
            }

            throw new Exception(
                    "Could not save the user profile.", e);
        }

        return user;
    }

    // LOGIN
    public Map<String, Object> login(
            String email,
            String password) throws Exception {

        // The Web API key is configured as an environment variable.
        String apiKey = System.getenv("FIREBASE_WEB_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "FIREBASE_WEB_API_KEY is not configured.");
        }

        String url =
                "https://identitytoolkit.googleapis.com/v1/"
                + "accounts:signInWithPassword?key=" + apiKey;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);
        requestBody.put("returnSecureToken", true);

        String json = gson.toJson(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IllegalArgumentException(
                    "Login failed. Check your credentials "
                    + "and Firebase Authentication settings.");
        }

        Type responseType =
                new TypeToken<Map<String, Object>>() {}.getType();

        return gson.fromJson(response.body(), responseType);
    }
}