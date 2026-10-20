package main;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import service.DBconnect;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
System.out.println("GYMAPP server starting");
		
		try {
			Firestore db=DBconnect.getFirestore();
			//DocumentSnapshot document=(DocumentSnapshot) db.collection("workouts")
			//		.document("workout001")
			//		.get()
			//		.get();
			
			
			 QuerySnapshot querySnapshot = db.collection("users").get().get();
	            
	        // lista completa de documentos
	        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
	            
			//if (document.exists()) {
			//	System.out.println("Connected successfully!");
			//	System.out.println("Workout:"+document.getData());
			//}
			//else {
			//	System.out.println("Connected but workout001 was not found");
			//}
			
	        
	        System.out.println("--- Lista de usuarios en la base de datos ---");
            
            // Recorrer cada documento encontrado con un bucle
            for (QueryDocumentSnapshot document : documents) {
                // Imprime el ID del documento y todo su mapa de campos/datos
                System.out.println("ID del usuario: " + document.getId());
                System.out.println("Datos: " + document.getData());
                System.out.println("-------------------------------------");
            }
            
            if (documents.isEmpty()) {
                System.out.println("La colección está vacía o no tiene documentos.");
            }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
