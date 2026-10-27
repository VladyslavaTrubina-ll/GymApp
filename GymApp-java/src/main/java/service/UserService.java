package service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import java.util.HashMap;
import java.util.Map;

public class UserService {

    /**
     * Recibe de forma asíncrona los datos del usuario en función de su UID desde Firestore.
     */
    public ApiFuture<DocumentSnapshot> getUserProfileAsync(String uid) throws Exception {
        Firestore db = DBconnect.getFirestore();
        return db.collection("users").document(uid).get();
    }

    /**
     * Actualiza de forma asíncrona los datos del perfil en Firestore.
     */
    public ApiFuture<WriteResult> updateUserProfileAsync(String uid, String firstName, String lastName, String email, String birthDate) throws Exception {
        Firestore db = DBconnect.getFirestore();
        
        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", firstName);
        updates.put("lastName", lastName);
        updates.put("email", email);
        updates.put("birthDate", birthDate);
        
        // El método .set() devuelve ApiFuture<WriteResult>
        return db.collection("users").document(uid).set(updates, SetOptions.merge());
    }
}
