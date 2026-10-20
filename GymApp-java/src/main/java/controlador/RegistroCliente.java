/**
 * 
 */
package controlador;

/**
 * 
 */
public class RegistroCliente {
	
	
	/**
	 * Funcion que verifica que los textos escritos en los campos de password coincidan
	 * @param pass1 String
	 * @param pass2 String
	 * @return String
	 */
	public static String verificarPass(String pass1, String pass2) { //valorar pasar el boton como parametro para inhabilitarlo a falta de otra forma para inhabilitarlo
		String responsePass;
		
		if(pass1.equals(pass2)) {
			responsePass = "Las contraseñas coinciden";
		}
		else {
			responsePass = "Las contraseñas no coinciden";
		}
		return responsePass;
	}
	
	//  verificar que todos los campos esten rellenados
	
	public static boolean camposCompletos(String nombre, String apellidos, String email, String pass, String fecNac) {
		boolean completos = false;
		
		
		return completos;
	}

}
