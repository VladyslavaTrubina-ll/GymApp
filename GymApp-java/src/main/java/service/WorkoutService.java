
package service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.DocumentSnapshot;

import java.io.IOException;
import java.util.List;
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

            // Read the user's level
            Long levelValue = userDocument.getLong("level");
            int userLevel = levelValue != null
                    ? levelValue.intValue() : 0;

            // Level 0 users can access only level 0 workouts
            if (userLevel == 0) {
                return db.collection("workouts")
                        .whereEqualTo("nivel", 0)
                        .get()
                        .get()
                        .getDocuments();
            }

            // Users above level 0 can access their level and lower
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
}