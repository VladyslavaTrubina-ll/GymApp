/**
 * 
 */
package controlador;

/**
 * 
 */
public class RegistroCliente {
	
	//	verificar que las contraseñas coinciden
	public static String verificarPass(String pass1, String pass2) { //valorar pasar el boton como parametro para inhabilitarlo
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
	

}
