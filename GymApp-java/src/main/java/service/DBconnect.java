package service;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class DBconnect {

private static Firestore firestore;
	
	public static Firestore getFirestore() throws IOException {
	if (firestore == null) {
		
		//	verificar el archivo segun el miembro del equipo que realice la conexion
		String key = "src/main/java/adminsdk/gymapp-af797-firebase-adminsdk-fbsvc-793f35f030.json";
		
		// Cargar las credenciales desde el archivo JSON
		FileInputStream serviceAccount =
		new FileInputStream(key);
		
		FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setProjectId("gymapp-af797")
				.build();
		
		// Inicializar FirebaseApp (solo una vez)
		if (FirebaseApp.getApps().isEmpty()) {
			FirebaseApp.initializeApp(options);
		}
		
		// Obtener la instancia de Firestore
		firestore = FirestoreClient.getFirestore();
	}
	return firestore;
	}
}
