package test;

import java.util.Map;

import service.AuthService;

public class testLogin {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 System.out.println("=== INICIANDO PRUEBA DE LOGIN ===");

	        // 1. Verificar si la variable de entorno está disponible
		 //FIREBASE_WEB_API_KEY = AIzaSyABdvQlNQVNptzkwphI8th_Q2q9aqUMPSI
	        String apiKey = System.getenv("FIREBASE_WEB_API_KEY");
	        if (apiKey == null || apiKey.isBlank()) {
	            System.err.println("ERROR: La variable de entorno FIREBASE_WEB_API_KEY no está configurada.");
	            System.err.println("Por favor, configúrala antes de ejecutar este test.");
	            return;
	        }

	        AuthService authService = new AuthService();

	        // REQUISITO: datos reales en firebase
	        String emailValido = "gymapp.test@example.com"; 
	        String passwordValida = "TestPassword123";

	        // ---- PRUEBA 1: Login Exitoso ----
	        try {
	            System.out.println("\n[Test 1] Intentando login con credenciales correctas...");
	            Map<String, Object> resultado = authService.login(emailValido, passwordValida);
	            
	            System.out.println("¡ÉXITO! Login completado.");
	            System.out.println("ID Token obtenido: " + resultado.get("idToken"));
	            System.out.println("Local ID (UID): " + resultado.get("localId"));
	            System.out.println("Expiración (segundos): " + resultado.get("expiresIn"));
	            
	        } catch (Exception e) {
	            System.out.println("FALLÓ el Test 1 (Login exitoso): " + e.getMessage());
	        }

	        // ---- PRUEBA 2: Login Fallido ----
	        try {
	            System.out.println("\n[Test 2] Intentando login con contraseña incorrecta...");
	            authService.login(emailValido, "ContrasenaEquivocada");
	            
	            System.out.println("FALLÓ el Test 2: Se esperaba una excepción pero el login fue exitoso.");
	        } catch (IllegalArgumentException e) {
	            System.out.println("¡ÉXITO! El sistema capturó correctamente el error esperado:");
	            System.out.println("Mensaje de error: " + e.getMessage());
	        } catch (Exception e) {
	            System.out.println("FALLÓ el Test 2: Se lanzó una excepción inesperada: " + e.getClass().getName());
	        }

	        System.out.println("\n=== PRUEBAS FINALIZADAS ===");
	}

}
