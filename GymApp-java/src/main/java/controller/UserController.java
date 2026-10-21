package controller;

import service.AuthService;
import service.DBconnect;

import java.util.HashMap;
import java.util.Map;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;

import model.User;

public class UserController {

	// communication with Firebase
    private AuthService authService;

    //object when the controller is initialized
    public UserController() {
        authService = new AuthService();
    }

    
 // Handles the login request received from the view
    //object contains the object returned from firebase
    public Map<String, Object> login(String email, String password)
            throws Exception {

    	//no empty fields
        if (email == null || email.trim().isEmpty()
                || password == null || password.isEmpty()) {
        	
        	//field is missing
            throw new IllegalArgumentException(
                    "Email and password are required.");
        }
        
     // Sends the login request to AuthService
        // Removes unnecessary spaces from the email
        return authService.login(email.trim(), password);
    }
    
    
    public User register(
            String firstName,
            String lastName,
            String email,
            String password,
            String birthDate) throws Exception {

        // Initialize Firebase before using Authentication
        Firestore db = DBconnect.getFirestore();

        // Create the account in Firebase Authentication
        UserRecord.CreateRequest authRequest =
                new UserRecord.CreateRequest()
                        .setEmail(email)
                        .setPassword(password)
                        .setDisplayName(firstName + " " + lastName);

        UserRecord firebaseUser =
                FirebaseAuth.getInstance().createUser(authRequest);

        String uid = firebaseUser.getUid();

        // Create the user profile
        User user = new User(
                uid, firstName, lastName, email, birthDate, 0
        );

        // Save the profile in Firestore
        Map<String, Object> profile = new HashMap<>();
        profile.put("firstName", firstName);
        profile.put("lastName", lastName);
        profile.put("email", email);
        profile.put("birthDate", birthDate);
        profile.put("level", 0);

        db.collection("users").document(uid).set(profile).get();

        return user;
    }
}